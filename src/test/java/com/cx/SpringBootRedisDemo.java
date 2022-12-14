package com.cx;

import com.cx.util.RedisLockUtils;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisCallback;
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
    @Autowired
    private RedisLockUtils<String> redisLockUtils;

    @Test
    public void test14() throws InterruptedException {
        String lockKey = UUID.randomUUID().toString();
        for (int i=0;i<3;i++){
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(()->{
                redisLockUtils.doWork(lockKey,TimeUnit.SECONDS,3,"chenxiang",this::consumer);
            }).start();
        }
        TimeUnit.SECONDS.sleep(100);

    }

    private void consumer(String s){
        System.out.println("consumer:"+s);
    }
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
    public void test13(){
        // string????????????
       /* BoundValueOperations<String, Object> boundValueOperations = redisTemplate.boundValueOps("key1");
        boundValueOperations.set("value1");
        System.out.println(boundValueOperations.get());
        System.out.println(boundValueOperations.getAndSet("value"));
        System.out.println(boundValueOperations.get());
        // increment?????? ????????????????????????
        // System.out.println(boundValueOperations.increment(1));
        System.out.println(boundValueOperations.setIfAbsent("value3"));
        System.out.println(boundValueOperations.size());
        redisTemplate.opsForValue().set("key2",1);
        System.out.println(redisTemplate.opsForValue().increment("key2", 1));
        System.out.println(redisTemplate.opsForValue().size("key2"));*/
        redisTemplate.opsForValue().set("key1","value4");
        redisTemplate.opsForValue().set("key2","value1");
        redisTemplate.opsForValue().set("key3","value1");
        System.out.println(redisTemplate.delete(Lists.newArrayList("key2", "key3")));
        //System.out.println(redisTemplate.keys("*"));
        System.out.println(redisTemplate.type("key1"));

    }

    @Test
    public void test1() throws Exception {
        //1.???????????????
        //redisTemplate.opsForValue()
        //2.??????list
        //redisTemplate.opsForList()
        //3.??????set
        //redisTemplate.opsForSet()
        //4.??????hash
        //redisTemplate.opsForHash()
        //5.??????zset
        //redisTemplate.opsForZSet()
        //6.HyperLogLog
        //redisTemplate.opsForHyperLogLog()
        //7.??????geo
        //redisTemplate.opsForGeo();
        // 8.??????cluster
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
       redisTemplate.opsForValue().set("mykey","??????");
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
        // 1.???????????????key??????????????? ????????????true??????????????????false
        Boolean hasKey = redisTemplate.hasKey("k1");
        System.out.println(hasKey);
        // 2.????????????key????????????
        System.out.println(redisTemplate.opsForValue().get("c1"));
        // 3.????????????key
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
