package cn.wolfcode.shop.memeber.web.resolver;

import cn.wolfcode.commons.util.CookUtils;
import cn.wolfcode.shop.memeber.domain.User;
import cn.wolfcode.shop.memeber.redis.MemberServerKeyPrefix;
import cn.wolfcode.shop.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = CookUtils.getCookie(request, CookUtils.USER_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return redisService.get(MemberServerKeyPrefix.USER_TOKEN, token, User.class);
    }
}
