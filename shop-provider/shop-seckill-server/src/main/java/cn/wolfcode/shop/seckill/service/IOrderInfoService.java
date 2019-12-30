package cn.wolfcode.shop.seckill.service;

import cn.wolfcode.shop.seckill.domain.OrderInfo;

public interface IOrderInfoService {
    String seckill(Long userId, Long seckillId);

    OrderInfo selectByOrderNo(String orderNo);

    void cancelOrder(Long seckillId, String orderNo);

    OrderInfo findByOrderNo(String orderNo);

    void changePayStatus(String out_trade_no, Integer statusAccountPaid);
}
