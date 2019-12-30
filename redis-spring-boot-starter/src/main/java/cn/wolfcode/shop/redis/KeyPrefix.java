package cn.wolfcode.shop.redis;

public interface KeyPrefix {

    String getPrefix();

    int getExpireSeconds();
}
