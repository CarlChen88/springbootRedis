package com.cx.util;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLock {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public boolean lock(String lockKey, TimeUnit timeUnit, long timeout){
        if(redisTemplate.opsForValue().setIfAbsent(lockKey,"chenxiang")){
            redisTemplate.expire(lockKey,timeout,timeUnit);
            return true;
        }
        return false;
    }

    public boolean unlock(String lockKey){
        String luaScript ="if " +
                "  redis.call('get', KEYS[1]) == ARGV[1] " +
                "then " +
                "  return redis.call('del', KEYS[1]) " +
                "else " +
                "  return 0 " +
                "end";
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Boolean.class);
        redisScript.setScriptText(luaScript);
        List<String> keys = Lists.newArrayList();
        keys.add(lockKey);
        Boolean execute = redisTemplate.execute(redisScript, keys,"chenxiang");
        return execute;
    }
}
