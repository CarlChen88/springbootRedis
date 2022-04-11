package com.cx.distribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 */
@Component
public class DistributedLock {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public String lockWithTimeout(String lockName, long acquireTimeout, long timeout) {
        String retIdentifier = null;
        // 锁名
        String lockKey = "lock:"+ lockName;
        // 随机生成一个value
        String identifier = UUID.randomUUID().toString();
        //获取锁的超时时间
        long end = System.currentTimeMillis() + acquireTimeout;
        //自旋
        while (System.currentTimeMillis() < end) {
            if (redisTemplate.opsForValue().setIfAbsent(lockKey,identifier)){
                redisTemplate.expire(lockKey, timeout, TimeUnit.MILLISECONDS);
                retIdentifier = identifier;
                return retIdentifier;
            }
        }
        return retIdentifier;
    }

}
