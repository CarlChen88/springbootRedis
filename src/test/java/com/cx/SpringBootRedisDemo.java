package com.cx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringBootRedisDemo {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Test
    public void test12() throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("one","one");
        map.put("two","two");
        redisTemplate.opsForValue().multiSet(map);
        List<String> list = new ArrayList<>();
        list.add("one");
        List<Object> objects = redisTemplate.opsForValue().multiGet(list);
        System.out.println(objects);
        DataType one = redisTemplate.type("one");
    }

    @Test
    public void test1() throws Exception {
        //1.操作字符串
        //redisTemplate.opsForValue()
        //2.操作list
        //redisTemplate.opsForList()
        //3.操作set
        //redisTemplate.opsForSet()
        //4.操作hash
        //redisTemplate.opsForHash()
        //5.操作zset
        //redisTemplate.opsForZSet()
        //6.HyperLogLog
        //redisTemplate.opsForHyperLogLog()
        //7.操作geo
        //redisTemplate.opsForGeo();
        // 8.操作cluster
       // redisTemplate.opsForCluster();

       /* RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        redisConnection.flushAll();
        redisConnection.flushDb();*/

        /**
         * 127.0.0.1:6379> keys *
         * (empty list or set)
         * 127.0.0.1:6379> keys *
         * 1) "\xac\xed\x00\x05t\x00\x05mykey"
         * 127.0.0.1:6379> key
         */
       redisTemplate.opsForValue().set("mykey","陈祥");
       Object object = redisTemplate.opsForValue().get("mykey");
       System.out.println(object);

       Boolean flag = redisTemplate.opsForValue().setIfAbsent("kkk","vvvv");
        System.out.println(flag);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        String key = sdf.format(new Date());
        for ( int i =0 ;i <3 ; i++) {
            redisTemplate.opsForValue().increment(key,1);
        }
        long num = redisTemplate.opsForValue().increment(key,1);
        System.out.println(num);
        if (redisTemplate.expire(key, 10, TimeUnit.SECONDS)) {
            System.out.println("================================");
        }
        if(num > 5) {
            throw new Exception();
        }
    }


    @Test
    public void test2() {
        redisTemplate.opsForSet().add("set1","v1","v2");
        redisTemplate.opsForSet().add("set2","v1","v3");
        System.out.println(redisTemplate.opsForSet().union("set1","set2"));
    }

    @Test
    public void test3() {
        System.out.println(redisTemplate.opsForValue().setIfAbsent("k8", "v1"));
    }

    @Test
    public void test4(){
        // 1.判断是否有key所对应的值 有则返回true，没有则返回false
        Boolean hasKey = redisTemplate.hasKey("k1");
        System.out.println(hasKey);
        // 2.有则取出key对应的值
        System.out.println(redisTemplate.opsForValue().get("c1"));
        // 3.删除单个key
        redisTemplate.opsForValue().set("cx","chenxiang");
        //System.out.println(redisTemplate.delete("cx"));

        //System.out.println(redisTemplate.expire("cx",10, TimeUnit.SECONDS));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, 10000);
        System.out.println(redisTemplate.expireAt("cx", calendar.getTime()));

        Set<String> keys = redisTemplate.keys("bit*");
        System.out.println(keys);
        System.out.println(redisTemplate.type("cx"));
    }
}
