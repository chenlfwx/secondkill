package cn.wolfcode.shop.redis;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = "shop.redis")
public class RedisProperties {

    private String host = "localhost";
    private int port = 6379;
    private int timeout = 10;
    private String password;
    private int poolMaxTotal = 500;
    private int poolMaxIdle = 500;
    private int poolMaxWait = 500;
}
