package cn.wolfcode.commons.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class CommonControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public Result handleError(BusinessException e) {
        return Result.error(e.getCodeMsg());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleArgumentError(MethodArgumentTypeMismatchException e) {
        return Result.error(HttpStatus.BAD_REQUEST.value(), "请求参数类型不匹配！");
    }

    @ExceptionHandler(Exception.class)
    public Result handleError(Exception e) {
        return Result.defaultError(e.getMessage());
    }
}
