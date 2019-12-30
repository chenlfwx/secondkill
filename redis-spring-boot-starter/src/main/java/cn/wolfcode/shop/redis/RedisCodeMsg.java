package cn.wolfcode.shop.redis;

import cn.wolfcode.commons.util.CodeMsg;

public class RedisCodeMsg extends CodeMsg {

    private RedisCodeMsg(int code, String msg) {
        super(code, msg);
    }

    public static final RedisCodeMsg REDIS_ERROR = new RedisCodeMsg(500011, "网络超时，请稍后再试！");
}
