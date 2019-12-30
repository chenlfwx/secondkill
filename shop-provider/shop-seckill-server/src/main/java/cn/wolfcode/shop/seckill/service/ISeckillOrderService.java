package cn.wolfcode.shop.seckill.service;

import cn.wolfcode.shop.seckill.domain.SeckillOrder;

public interface ISeckillOrderService {

    SeckillOrder findByUserIdAndSeckillId(Long userId,Long seckillId);

    void createSeckillOrder(Long userId, Long seckillId, String orderNo);
}
