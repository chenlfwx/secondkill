package cn.wolfcode.shop.memeber.feign;


import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.memeber.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenFeignClient implements TokenFeignApi {

    @Autowired
    private IUserService userService;

    @Override
    public Result<Boolean> refresh(@RequestParam("token") String token) {
        return Result.success(userService.refresh(token));
    }
}
