package cn.wolfcode.shop.seckill.service.impl;

import cn.wolfcode.commons.util.BusinessException;
import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.goods.domain.Goods;
import cn.wolfcode.shop.goods.feign.GoodsFeignApi;
import cn.wolfcode.shop.redis.RedisService;
import cn.wolfcode.shop.seckill.domain.SeckillGood;
import cn.wolfcode.shop.seckill.mapper.SeckillGoodMapper;
import cn.wolfcode.shop.seckill.mq.MQConstants;
import cn.wolfcode.shop.seckill.redis.SeckillServerKeyPrefix;
import cn.wolfcode.shop.seckill.result.SeckillCodeMsg;
import cn.wolfcode.shop.seckill.service.ISeckillGoodService;
import cn.wolfcode.shop.seckill.vo.SeckillGoodVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SeckillGoodServiceImpl implements ISeckillGoodService {

    @Autowired
    private SeckillGoodMapper seckillGoodMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsFeignApi goodsFeignApi;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<SeckillGoodVO> query() {
        // 1、查询出所有秒杀商品中的数据
        List<SeckillGood> seckillGoodList = seckillGoodMapper.query();
        return getSeckillGoodVO(seckillGoodList);
    }

    @Override
    public void sync(Long seckillId) {
        int stockCount = seckillGoodMapper.getGoodStock(seckillId);
        if (stockCount > 0) {
            //把预库存的变成真实库存的值.
            redisService.set(SeckillServerKeyPrefix.SECKILL_STOCK, seckillId + "", stockCount);
            //发布取消本地标识的消息
            rabbitTemplate.convertAndSend(MQConstants.SECKILL_OVER_SIGN_PUBSUB_EX, "", seckillId);
        }
    }

    @Override
    public SeckillGood selectByPrimaryKey(Long seckillId) {
        return seckillGoodMapper.selectByPrimaryKey(seckillId);
    }

    @Override
    public int descStock(Long seckillId) {
        return seckillGoodMapper.updateStock(seckillId);
    }


    public SeckillGoodVO findBySeckillId(Long seckillId) {
        SeckillGood seckillGood = seckillGoodMapper.selectByPrimaryKey(seckillId);
        if (seckillGood == null) {
            return null;
        }
        List<SeckillGood> seckillGoodList = new ArrayList<>();
        seckillGoodList.add(seckillGood);
        List<SeckillGoodVO> seckillGoodVO = getSeckillGoodVO(seckillGoodList);
        return seckillGoodVO.size() > 0 ? seckillGoodVO.get(0) : null;
    }

    @Override
    public SeckillGoodVO findByCache(Long seckillId) {
        return redisService.hget(SeckillServerKeyPrefix.SECKILL_GOOD, "", seckillId + "", SeckillGoodVO.class);
    }

    @Override
    public List<SeckillGoodVO> queryByCache() {
        Map<String, SeckillGoodVO> seckillGoodVOMap = redisService.hgetAll(SeckillServerKeyPrefix.SECKILL_GOOD, "", SeckillGoodVO.class);
        return new ArrayList<>(seckillGoodVOMap.values());
    }

    @Override
    public void incrGoodStock(Long seckillId) {
        seckillGoodMapper.incrStock(seckillId);
    }


    public List<SeckillGoodVO> getSeckillGoodVO(List<SeckillGood> seckillGoodList) {
        // 2、秒杀中有场次，需要去重
        Set<Long> idSet = new HashSet<>();
        for (int i = 0; i < seckillGoodList.size(); i++) {
            idSet.add(seckillGoodList.get(i).getGoodId());
        }
        List<Long> ids = new ArrayList<>(idSet);
        // 3、远程调用获取所有的商品信息
        Result<List<Goods>> listResult = goodsFeignApi.queryByIds(ids);
        // 为null是走了熔断，有错误是错误调用出现错误
        if (listResult == null || listResult.hasError()) {
            throw new BusinessException(SeckillCodeMsg.RMI_ERROR);
        }
        List<Goods> result = listResult.getData();
        Map<Long, Goods> map = new HashMap<>();
        for (int i = 0; i < result.size(); i++) {
            Goods good = result.get(i);
            map.put(good.getId(), good);
        }
        // 4、内存中做聚合
        Goods good = null;
        List<SeckillGoodVO> seckillGoodVOS = new ArrayList<>();
        SeckillGoodVO seckillGoodVO = null;
        for (SeckillGood seckillGood : seckillGoodList) {
            good = map.get(seckillGood.getGoodId());
            seckillGoodVO = new SeckillGoodVO();
            seckillGoodVO.setGoodDetail(good.getGoodDetail());// 商品详情
            seckillGoodVO.setGoodImg(good.getGoodImg());// 商品图片
            seckillGoodVO.setGoodName(good.getGoodName());// 商品名称
            seckillGoodVO.setGoodPrice(good.getGoodPrice());// 商品原价
            seckillGoodVO.setGoodTitle(good.getGoodTitle());// 商品标题
            seckillGoodVO.setEndDate(seckillGood.getEndDate());// 秒杀结束时间
            seckillGoodVO.setGoodId(seckillGood.getGoodId());// 商品id
            seckillGoodVO.setSeckillPrice(seckillGood.getSeckillPrice());// 秒杀价格
            seckillGoodVO.setStartDate(seckillGood.getStartDate());// 秒杀开始时间
            seckillGoodVO.setStockCount(seckillGood.getStockCount());// 秒杀库存数量
            seckillGoodVO.setId(seckillGood.getId());//秒杀id
            seckillGoodVOS.add(seckillGoodVO);
        }
        return seckillGoodVOS;
    }
}
