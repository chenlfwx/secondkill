package cn.wolfcode.shop.seckill.web.config;

import cn.wolfcode.shop.memeber.web.resolver.UserArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
public class CustomerWebMvcConfigurer implements WebMvcConfigurer {

    @Bean
    public UserArgumentResolver userArgumentResolver() {
        return new UserArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver());
    }
}


