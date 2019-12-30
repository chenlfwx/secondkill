package cn.wolfcode.shop.zuul.filter;

import cn.wolfcode.commons.util.CookUtils;
import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.memeber.feign.TokenFeignApi;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenRefreshFilter extends ZuulFilter {

    @Autowired
    private TokenFeignApi tokenFeignApi;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        // 登录之后才拦截
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String token = CookUtils.getCookie(request, CookUtils.USER_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 刷新Redis中对应的有效时间，刷新Token的有效时间
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String token = CookUtils.getCookie(request, CookUtils.USER_TOKEN);
        Result<Boolean> result = tokenFeignApi.refresh(token);
        if (result != null && !result.hasError() && result.getData()) {
            CookUtils.add(currentContext.getResponse(), CookUtils.USER_TOKEN, token, CookUtils.DEFAULT_MAX_AGE);
        }
        return null;
    }
}
