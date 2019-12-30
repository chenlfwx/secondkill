package cn.wolfcode.shop.seckill.controller;

import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.memeber.domain.User;
import cn.wolfcode.shop.memeber.web.resolver.LoginUser;
import cn.wolfcode.shop.redis.RedisService;
import cn.wolfcode.shop.seckill.domain.OrderInfo;
import cn.wolfcode.shop.seckill.mq.MQConstants;
import cn.wolfcode.shop.seckill.mq.OrderMessage;
import cn.wolfcode.shop.seckill.redis.SeckillServerKeyPrefix;
import cn.wolfcode.shop.seckill.result.SeckillCodeMsg;
import cn.wolfcode.shop.seckill.service.IOrderInfoService;
import cn.wolfcode.shop.seckill.service.ISeckillGoodService;
import cn.wolfcode.shop.seckill.service.ISeckillOrderService;
import cn.wolfcode.shop.seckill.vo.SeckillGoodVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/order")
public class OrderInfoController {

    @Autowired
    private ISeckillGoodService seckillGoodService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderInfoService orderInfoService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static ConcurrentMap<Long, Boolean> isOverSignMap = new ConcurrentHashMap<>();

    /**
     * 防止别人刷接口
     * @param seckillId
     * @param user
     * @param uuid
     * @param path
     * @return
     */
    @RequestMapping("/{path}/seckill")
    public Result<String> seckill(@RequestParam("seckillId") Long seckillId, @LoginUser User user, @RequestParam("uuid") String uuid, @PathVariable("path") String path) {

        if (StringUtils.isEmpty(path)) {
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        // 1、验证是否登录
        if (user == null) {
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        // 验证接口的正确
        String seckillPath = redisService.get(SeckillServerKeyPrefix.SECKILL_PATH, uuid, String.class);
        if (seckillPath == null || !seckillPath.equals(path)) {
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        Boolean isOverSign = isOverSignMap.get(seckillId);
        if (isOverSign != null && isOverSign) {
            return Result.error(SeckillCodeMsg.SECKILL_OVER);
        }
        // 2、验证是否是有效的秒杀ID(保证接口安全)
        SeckillGoodVO seckillGoodVO = seckillGoodService.findByCache(seckillId);
        if (seckillGoodVO == null) {
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        // 3、判断是否到达开始开始时间，是否活动结束
        Date now = new Date();
        long time = now.getTime();
        if (time < seckillGoodVO.getStartDate().getTime() || time > seckillGoodVO.getEndDate().getTime()) {
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        // 4、用户不能重复下单，一次只能购买一件
        if (redisService.existKey(SeckillServerKeyPrefix.SECKILL_ORDER, user.getId() + ":" + seckillId)) {
            return Result.error(SeckillCodeMsg.REPEAT_ERROR);
        }

        // 库存是否足够
        if (seckillGoodVO.getStockCount() <= 0) {
            return Result.error(SeckillCodeMsg.SECKILL_OVER);
        }
        // 控制秒杀人数
        Long count = redisService.decr(SeckillServerKeyPrefix.SECKILL_STOCK, seckillId + "");
        if (count != null && count < 0) {
            isOverSignMap.put(seckillId, true);
            return Result.error(SeckillCodeMsg.SECKILL_OVER);
        }
        // 秒杀逻辑，原子性
        OrderMessage orderMessage = new OrderMessage(user.getId(), seckillId, uuid);
        rabbitTemplate.convertAndSend(MQConstants.ORDER_PEDDING_QUEUE, orderMessage);
        return Result.success("下单操作成功，等待下单操作结果！");
    }

    @RequestMapping("/find")
    public Result<OrderInfo> find(String orderNo, @LoginUser User user) {
        // 用户必须要登录
        if (user == null) {
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        // 防止别人乱填订单
        OrderInfo orderInfo = orderInfoService.selectByOrderNo(orderNo);
        if (orderInfo == null) {
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        // 只有是自己的订单才能看到
        if (!user.getId().equals(orderInfo.getUserId())) {
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        return Result.success(orderInfo);
    }
}
