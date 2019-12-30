package cn.wolfcode.shop.memeber.feign;

import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.memeber.feign.hystrix.TokenFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-server", fallback = TokenFeignHystrix.class)
public interface TokenFeignApi {

    @RequestMapping("/refresh")
    Result<Boolean> refresh(@RequestParam("token")String token);
}
