package cn.wolfcode.shop.zuul.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.RATE_LIMIT_EXCEEDED;


@Component
public class CustomerErrorZuulFilter extends ZuulFilter {

    @Autowired
    private ObjectMapper objectMapper;

    protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";

    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getThrowable() != null
                && !ctx.getBoolean(SEND_ERROR_FILTER_RAN, false);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        Map<String, Object> result = new HashMap<>();
        // See RateLimitPreFilter
        if ("true".equals(currentContext.get(RATE_LIMIT_EXCEEDED))) {
            // 限流异常
            result.put("code", HttpStatus.TOO_MANY_REQUESTS.value());
            result.put("msg", "您操作的频率太快，请稍后再试！");
        } else {
            // 其他的异常
            result.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.put("msg", "系统繁忙，请稍候再试！");
        }
        try {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            PrintWriter writer = response.getWriter();
            writer.println(objectMapper.writeValueAsString(result));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
