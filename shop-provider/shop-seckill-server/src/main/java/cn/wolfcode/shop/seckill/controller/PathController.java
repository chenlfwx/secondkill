package cn.wolfcode.shop.seckill.controller;

import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.redis.RedisService;
import cn.wolfcode.shop.seckill.redis.SeckillServerKeyPrefix;
import cn.wolfcode.shop.seckill.result.SeckillCodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/path")
public class PathController {

    @Autowired
    private RedisService redisService;

    @RequestMapping("/getPath")
    public Result<String> getPath(@RequestParam("uuid") String uuid, @RequestParam("result") Integer result) {
        if (StringUtils.isEmpty(uuid)) {
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        if (result == null) {
            return Result.error(SeckillCodeMsg.VERIFYCODE_ERROR);
        }
        Integer verifyCodeAnswer = redisService.get(SeckillServerKeyPrefix.SECKILL_VERIFYCODE, uuid, Integer.class);
        if (!result.equals(verifyCodeAnswer)) {
            return Result.error(SeckillCodeMsg.OP_ERROR);
        }
        String path = UUID.randomUUID().toString().replace("-", "");
        redisService.set(SeckillServerKeyPrefix.SECKILL_PATH, uuid, path);
        return Result.success(path);
    }
}
