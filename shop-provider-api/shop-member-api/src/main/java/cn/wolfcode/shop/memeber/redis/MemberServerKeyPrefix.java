package cn.wolfcode.shop.memeber.redis;

import cn.wolfcode.shop.redis.KeyPrefix;


public class MemberServerKeyPrefix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public MemberServerKeyPrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public int getExpireSeconds() {
        return expireSeconds;
    }

    public static final MemberServerKeyPrefix USER_TOKEN = new MemberServerKeyPrefix(1800, "userToken:");
}
