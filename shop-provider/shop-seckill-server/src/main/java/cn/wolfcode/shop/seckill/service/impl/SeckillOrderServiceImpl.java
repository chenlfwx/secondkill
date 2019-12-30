package cn.wolfcode.shop.seckill.service.impl;

import cn.wolfcode.shop.seckill.domain.SeckillOrder;
import cn.wolfcode.shop.seckill.mapper.SeckillOrderMapper;
import cn.wolfcode.shop.seckill.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SeckillOrderServiceImpl implements ISeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Override
    public SeckillOrder findByUserIdAndSeckillId(Long userId, Long seckillId) {
        return seckillOrderMapper.findByUserIdAndSeckillId(userId, seckillId);
    }

    @Override
    public void createSeckillOrder(Long userId, Long seckillId, String orderNo) {
        SeckillOrder order = new SeckillOrder();
        order.setOrderNo(orderNo);
        order.setSeckillId(seckillId);
        order.setUserId(userId);
        seckillOrderMapper.insert(order);
    }
}
