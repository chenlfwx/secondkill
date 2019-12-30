package cn.wolfcode.shop.seckill.redis;

import cn.wolfcode.shop.redis.KeyPrefix;

public class SeckillServerKeyPrefix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public int getExpireSeconds() {
        return expireSeconds;
    }


    public SeckillServerKeyPrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public static final SeckillServerKeyPrefix SECKILL_STOCK = new SeckillServerKeyPrefix(0, "seckillStock:");
    public static final SeckillServerKeyPrefix SECKILL_GOOD = new SeckillServerKeyPrefix(0, "seckillGoodHash");
    public static final SeckillServerKeyPrefix SECKILL_ORDER = new SeckillServerKeyPrefix(0, "seckillOrder:");
    public static final SeckillServerKeyPrefix SECKILL_PATH = new SeckillServerKeyPrefix(2, "seckillPath:");
    public static final SeckillServerKeyPrefix SECKILL_VERIFYCODE = new SeckillServerKeyPrefix(60, "seckillVerifyCode:");
}
