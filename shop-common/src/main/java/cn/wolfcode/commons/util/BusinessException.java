package cn.wolfcode.commons.util;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {


    private static final long serialVersionUID = -7255809696280654753L;

    private CodeMsg codeMsg;

    public BusinessException(CodeMsg codeMsg) {
        super(codeMsg.getMsg());
        this.codeMsg = codeMsg;
    }


    public BusinessException() {
    }
}
