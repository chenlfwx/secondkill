package cn.wolfcode.shop.seckill.service;

import cn.wolfcode.shop.seckill.domain.SeckillGood;
import cn.wolfcode.shop.seckill.vo.SeckillGoodVO;

import java.util.List;

public interface ISeckillGoodService {

    List<SeckillGoodVO> query();

    void sync(Long seckillId);

    SeckillGood selectByPrimaryKey(Long seckillId);

    int descStock(Long seckillId);

    SeckillGoodVO findBySeckillId(Long seckillId);

    SeckillGoodVO findByCache(Long seckillId);

    List<SeckillGoodVO> queryByCache();

    void incrGoodStock(Long seckillId);
}
