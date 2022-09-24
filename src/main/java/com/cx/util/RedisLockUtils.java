package com.cx.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component
public class RedisLockUtils<T> {
    @Autowired
    private RedisLock redisLock;

    public void doWork(String lockKey, TimeUnit timeUnit, long timeout, T t, Consumer<T> consumer){
        try {
            if(redisLock.lock(lockKey,timeUnit,timeout)){
                System.out.println("获取到了锁");
                consumer.accept(t);
            }else{
                System.out.println("没有获取到锁");
            }
        }catch (Exception e){
            throw e;
        }finally {
            redisLock.unlock(lockKey);
        }
    }
}
