package cn.wolfcode.shop.memeber.web.advice;

import cn.wolfcode.commons.util.CommonControllerAdvice;
import cn.wolfcode.commons.util.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员服务统一异常处理
 */
@RestControllerAdvice
public class MemberServerControllerAdvice extends CommonControllerAdvice {


    @ExceptionHandler(BindException.class)
    public Result<Map<String, String>> handle(BindException e) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> fieldErrors = e.getFieldErrors();
        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError fieldError = fieldErrors.get(i);
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            map.put(field, defaultMessage);
        }
        return Result.error(Result.PARAM_ERROR, map);
    }
}