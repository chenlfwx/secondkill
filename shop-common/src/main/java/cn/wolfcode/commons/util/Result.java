package cn.wolfcode.commons.util;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    /** 状态码 **/
    private Integer code;

    /** 消息 **/
    private String msg;

    /** 数据 **/
    private T data;


    public static final int DEFAULT_SUCCESS_CODE = 200;

    public static final int DEFAULT_ERROR_CODE = 500;

    /** 参数错误 **/
    public static final int PARAM_ERROR = 500401;

    public static final String DEFAULT_SUCCESS_MSG = "处理成功";

    public static final String DEFAULT_ERROR_MSG = "处理失败";


    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(DEFAULT_SUCCESS_CODE, msg, data);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(DEFAULT_SUCCESS_CODE, null, data);
    }

    public static <T> Result<T> error(CodeMsg codeMsg) {
        return new Result<>(codeMsg.getCode(), codeMsg.getMsg(), null);
    }


    public static <T> Result<T> defaultError(String msg) {
        return new Result<>(DEFAULT_ERROR_CODE, msg, null);
    }

    public static <T> Result<T> error(T data) {
        return new Result<>(DEFAULT_ERROR_CODE, null, data);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(DEFAULT_ERROR_CODE, msg, null);
    }

    public static <T> Result<T> error(int code, T data) {
        return new Result<>(PARAM_ERROR, null, data);
    }

    public boolean hasError() {
        return !code.equals(DEFAULT_SUCCESS_CODE);
    }
}
