package cn.wolfcode.shop.seckill;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "cn.wolfcode.shop")
@MapperScan("cn.wolfcode.shop.seckill.mapper")
@EnableFeignClients("cn.wolfcode.shop.goods.feign")
@EnableHystrix
@EnableEurekaClient
public class SeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }
}
