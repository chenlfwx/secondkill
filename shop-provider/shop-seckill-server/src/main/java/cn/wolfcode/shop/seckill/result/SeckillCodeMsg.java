package cn.wolfcode.shop.seckill.result;

import cn.wolfcode.commons.util.CodeMsg;

public class SeckillCodeMsg extends CodeMsg {


    private SeckillCodeMsg(int code, String msg) {
        super(code, msg);
    }

    /** 调用远程方法出现异常 **/
    public static final SeckillCodeMsg RMI_ERROR = new SeckillCodeMsg(300332, "商品服务不可用！");

    public static final SeckillCodeMsg OP_ERROR = new SeckillCodeMsg(301333, "非法操作");

    public static final SeckillCodeMsg REPEAT_ERROR = new SeckillCodeMsg(301334, "您已经抢购过了，请不要重复抢购！");

    public static final SeckillCodeMsg SECKILL_OVER = new SeckillCodeMsg(301335, "秒杀已经结束！");

    public static final SeckillCodeMsg SECKILL_ERROR = new SeckillCodeMsg(301336, "下单失败！");

    public static final SeckillCodeMsg VERIFYCODE_ERROR = new SeckillCodeMsg(301337, "验证码不正确");

}
