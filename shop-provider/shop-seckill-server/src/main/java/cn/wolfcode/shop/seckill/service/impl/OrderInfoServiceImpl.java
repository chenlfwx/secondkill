package cn.wolfcode.shop.seckill.service.impl;

import cn.wolfcode.commons.util.BusinessException;
import cn.wolfcode.shop.seckill.domain.OrderInfo;
import cn.wolfcode.shop.seckill.mapper.OrderInfoMapper;
import cn.wolfcode.shop.seckill.result.SeckillCodeMsg;
import cn.wolfcode.shop.seckill.service.IOrderInfoService;
import cn.wolfcode.shop.seckill.service.ISeckillGoodService;
import cn.wolfcode.shop.seckill.service.ISeckillOrderService;
import cn.wolfcode.shop.seckill.util.IdGenerateUtil;
import cn.wolfcode.shop.seckill.vo.SeckillGoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@Transactional
public class OrderInfoServiceImpl implements IOrderInfoService {

    @Autowired
    private ISeckillGoodService seckillGoodService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ISeckillOrderService seckillOrderService;


    @Override
    public String seckill(Long userId, Long seckillId) {
        int count = seckillGoodService.descStock(seckillId);
        if (count <= 0) {
            throw new BusinessException(SeckillCodeMsg.SECKILL_OVER);
        }
        String orderNo = createOrder(userId, seckillId);
        seckillOrderService.createSeckillOrder(userId, seckillId, orderNo);
        return orderNo;
    }

    @Override
    public OrderInfo selectByOrderNo(String orderNo) {
        return orderInfoMapper.selectByOrderNo(orderNo);
    }

    @Transactional
    @Override
    public void cancelOrder(Long seckillId, String orderNo) {
        int count = orderInfoMapper.updateOrderCancelStatue(orderNo, OrderInfo.STATUS_CANCEL);
        if (count > 0) {
            seckillGoodService.incrGoodStock(seckillId);
            seckillGoodService.sync(seckillId);
        } else {
            // 已支付，不需要做任何事情
        }
    }

    @Override
    public OrderInfo findByOrderNo(String orderNo) {
        return orderInfoMapper.selectByOrderNo(orderNo);
    }

    @Override
    public void changePayStatus(String orderNo, Integer status) {
        orderInfoMapper.updatePayStatus(orderNo, status);
    }

    /**
     * @param userId
     * @param seckillId
     * @return 返回生成订单信息的编号
     */
    private String createOrder(Long userId, Long seckillId) {
        OrderInfo orderInfo = new OrderInfo();
        SeckillGoodVO seckillGoodVO = seckillGoodService.findBySeckillId(seckillId);
        orderInfo.setGoodName(seckillGoodVO.getGoodName());
        orderInfo.setGoodCount(1);
        orderInfo.setGoodImg(seckillGoodVO.getGoodImg());
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodId(seckillGoodVO.getGoodId());
        orderInfo.setGoodPrice(seckillGoodVO.getGoodPrice());
        orderInfo.setSeckillPrice(seckillGoodVO.getSeckillPrice());
        orderInfo.setUserId(userId);
        orderInfo.setOrderNo(String.valueOf(IdGenerateUtil.get().nextId()));// 订单编号
        orderInfoMapper.insert(orderInfo);
        return orderInfo.getOrderNo();
    }
}
