package cn.wolfcode.shop.seckill.controller;

import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.redis.RedisService;
import cn.wolfcode.shop.seckill.redis.SeckillServerKeyPrefix;
import cn.wolfcode.shop.seckill.service.ISeckillGoodService;
import cn.wolfcode.shop.seckill.vo.SeckillGoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckillGood")
public class SeckillGoodController {

    @Autowired
    private ISeckillGoodService seckillGoodService;

    @Autowired
    private RedisService redisService;

    @GetMapping("/query")
    public Result<List<SeckillGoodVO>> query() {
        return Result.success(seckillGoodService.queryByCache());
    }

    @RequestMapping("/find")
    public Result<SeckillGoodVO> find(Long seckillId) {
        return Result.success(seckillGoodService.findByCache(seckillId));
    }


    @RequestMapping("/initData")
    public Result<String> initData() {
        List<SeckillGoodVO> seckillGoodVOs = seckillGoodService.query();
        for (int i = 0; i < seckillGoodVOs.size(); i++) {
            SeckillGoodVO seckillGoodVO = seckillGoodVOs.get(i);
            String key = seckillGoodVO.getId() + "";
            redisService.set(SeckillServerKeyPrefix.SECKILL_STOCK, key, seckillGoodVO.getStockCount());
            redisService.hset(SeckillServerKeyPrefix.SECKILL_GOOD, "", key, seckillGoodVO);
        }
        return Result.success("init success");
    }
}
