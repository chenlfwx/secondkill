package cn.wolfcode.shop.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ConditionalOnClass({JedisPool.class, Jedis.class})
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = "shop.redis", name = "host")
public class RedisAutoConfiguration {

    private final RedisProperties redisProperties;

    public RedisAutoConfiguration(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }


    @Bean
    public JedisPool pool() throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisProperties.getPoolMaxTotal());
        config.setMaxIdle(redisProperties.getPoolMaxIdle());
        config.setMaxWaitMillis(redisProperties.getPoolMaxWait() * 1000);
        JedisPool jedisPool = new JedisPool(config, redisProperties.getHost(), redisProperties.getPort(), redisProperties.getTimeout(), redisProperties.getPassword());
        return jedisPool;
    }

    @Bean
    public RedisService redisService() {
        return new RedisService();
    }
}
