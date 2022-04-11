package com.cx.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean set(String key, Object value) {
        try{
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception exception) {
            return false;
        }
    }

    /**
     * 设置过期时间(秒)
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean set(String key, Object value,long time) {
        try{
            redisTemplate.opsForValue().set(key, value, time,TimeUnit.SECONDS);
            return true;
        }catch (Exception exception) {
            return false;
        }
    }

    public Object get(String key) {
        try{
            return  redisTemplate.opsForValue().get(key);
        }catch (Exception exception) {
            return null;
        }
    }

}
