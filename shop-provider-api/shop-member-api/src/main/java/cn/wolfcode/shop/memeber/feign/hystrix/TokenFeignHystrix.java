package cn.wolfcode.shop.memeber.feign.hystrix;

import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.memeber.feign.TokenFeignApi;
import org.springframework.stereotype.Component;


@Component
public class TokenFeignHystrix implements TokenFeignApi {
    @Override
    public Result<Boolean> refresh(String token) {
        return null;
    }
}
