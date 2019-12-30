package cn.wolfcode.shop.memeber.result;

import cn.wolfcode.commons.util.CodeMsg;

public class MemberServerCodeMsg extends CodeMsg {

    private MemberServerCodeMsg(Integer code, String msg) {
        super(code, msg);
    }

    public static MemberServerCodeMsg OP_ERROR = new MemberServerCodeMsg(500001, "非法操作");

    public static MemberServerCodeMsg LOGIN_ERROR = new MemberServerCodeMsg(500002, "用户名或密码错误");

}
