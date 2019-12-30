package cn.wolfcode.shop.redis;

import cn.wolfcode.commons.util.BusinessException;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    public <T> void set(KeyPrefix prefix, String key, T data) {
        Objects.requireNonNull(prefix);
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            String jsonString = JSON.toJSONString(data);
            if (prefix.getExpireSeconds() > 0) {
                jedis.setex(realKey, prefix.getExpireSeconds(), jsonString);
            } else {
                jedis.set(realKey, jsonString);
            }
        } catch (Exception e) {
            throw new BusinessException(RedisCodeMsg.REDIS_ERROR);
        }
    }


    public <T> T get(KeyPrefix prefix, String key, Class<T> type) {
        Objects.requireNonNull(type);
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            String jsonString = jedis.get(realKey);
            return JSON.parseObject(jsonString, type);
        } catch (Exception e) {
            throw new BusinessException(RedisCodeMsg.REDIS_ERROR);
        }
    }

    public void incr(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            jedis.incr(realKey);
        } catch (Exception e) {
            throw new BusinessException(RedisCodeMsg.REDIS_ERROR);
        }
    }

    public Long decr(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        } catch (Exception e) {
            throw new BusinessException(RedisCodeMsg.REDIS_ERROR);
        }
    }

    public Boolean existKey(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        } catch (Exception e) {
            throw new BusinessException(RedisCodeMsg.REDIS_ERROR);
        }
    }

    public void expire(KeyPrefix prefix, String key, int expireSecond) {
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            jedis.expire(realKey, expireSecond);
        } catch (Exception e) {
            throw new BusinessException(RedisCodeMsg.REDIS_ERROR);
        }
    }

    /**
     * hset操作
     */

    public <T> void hset(KeyPrefix prefix, String key, String field, T type) {
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            jedis.hset(realKey, field, JSON.toJSONString(type));
        } catch (Exception e) {
            throw new BusinessException(RedisCodeMsg.REDIS_ERROR);
        }
    }


    public <T> T hget(KeyPrefix prefix, String key, String field, Class<T> type) {
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            String value = jedis.hget(realKey, field);
            return JSON.parseObject(value, type);
        } catch (Exception e) {
            throw new BusinessException(RedisCodeMsg.REDIS_ERROR);
        }
    }

    public <T> Map<String, T> hgetAll(KeyPrefix prefix, String key, Class<T> type) {
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            Map<String, String> map = jedis.hgetAll(realKey);
            Map<String, T> result = new HashMap<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                result.put(entry.getKey(), JSON.parseObject(entry.getValue(), type));
            }
            return result;
        } catch (Exception e) {
            throw new BusinessException(RedisCodeMsg.REDIS_ERROR);
        }
    }
}
