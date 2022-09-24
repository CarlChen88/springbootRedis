package com.cx;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringBootRedisDemo2 {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void testKey(){
        //key的操作
    }

    @Test
    public void testString(){
        BoundValueOperations<String, String> valueOps = redisTemplate.boundValueOps("string");
        valueOps.set("v1");
        System.out.println(valueOps.setIfAbsent("v2"));
        System.out.println(valueOps.get());
        System.out.println(valueOps.getAndSet("1"));
        System.out.println(valueOps.get());
        valueOps.increment(-4);
        System.out.println(valueOps.get());
        valueOps.append("chenxiang");
        System.out.println(valueOps.get());
    }

    @Test
    public void testList(){
        //ListOperations<String, String> opsForList = redisTemplate.opsForList();
        System.out.println("redis 操作list数据类型");
        // 绑定list
        BoundListOperations<String, String> list = redisTemplate.boundListOps("list");
        // 队列从左边插入元素 堆栈模式
        list.leftPush("11");
        list.leftPush("22");
        list.leftPush("33");
        list.leftPush("44");
        // 从某个元素右侧插入队列 (33)某个元素则不插入
        list.leftPush("33","77");
        // 从队列左侧插入数组
        list.leftPushAll("88","99");
        // 从元素左侧插入某个元素 元素存在则不插入
        list.leftPushIfPresent("00");
        // 获取元素个数
        System.out.println(list.size());
        // 获取指定下标的元素
        System.out.println(list.index(0));
        //从左侧弹出队首元素
        System.out.println(list.leftPop());
        list.leftPop(2,TimeUnit.SECONDS);
        // 删除某个元素多少个
        list.remove(1,"88");
        // 裁剪指定范围的数据
        list.trim(0,10);
        // 指定下标位置设值
        list.set(1,"110");
        // 设置过期时间
        list.expire(1,TimeUnit.SECONDS);
        //取出全部元素
        List<String> allElements = list.range(0, -1);
        allElements.forEach(element->{
            System.out.println(element);
        });
    }

    @Test
    public void testHash(){
        System.out.println("redis 操作hash数据");
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps("hash");
        // 往hash里面插入数据
        hashOps.put("k1","v1");
        hashOps.put("k2","2");
        hashOps.putAll(Maps.newHashMap("k3","v3"));
        // 获取所有的key
        System.out.println(hashOps.keys());
        // 获取所有的value
        System.out.println(hashOps.values());
        System.out.println(hashOps.get("k1"));
        System.out.println(hashOps.multiGet(Lists.newArrayList("k2", "k3")));
        // 删除元素
        hashOps.delete("k1");
        hashOps.increment("k2",3);
        // 判断是否存在某个key
        System.out.println(hashOps.hasKey("k1"));
        hashOps.putIfAbsent("k3","v33");
        hashOps.putIfAbsent("k4","v4");
        //获取hash数据
        System.out.println(hashOps.entries());
    }

    @Test
    public void testSet(){
        BoundSetOperations<String, String> opsSet = redisTemplate.boundSetOps("set");
        // 向集合添加不重复的元素
        opsSet.add("one","two","three","one");
        // 判断某个元素是否在集合中
        System.out.println(opsSet.isMember("one"));
        BoundSetOperations<String, String> opsSet2 = redisTemplate.boundSetOps("set2");
        // 向集合添加不重复的元素
        opsSet2.add("three","four");
        BoundSetOperations<String, String> opsSet3 = redisTemplate.boundSetOps("set3");
        // 向集合添加不重复的元素
        opsSet3.add("five","one","six");
        // 取两个集合的差集
        System.out.println(opsSet.diff("set2"));
        // 取多个集合的差集
        System.out.println(opsSet.diff(Lists.newArrayList("set2", "set3")));
        // 取两个集合的差集并存储到新的(k1)集合里面
        opsSet.diffAndStore("set2","set4");
        // 取两个集合的并集
        System.out.println(opsSet.union("set2"));
        // 取两个集合的交集
        System.out.println(opsSet.intersect("set2"));
        //取出随机数
        System.out.println(opsSet.randomMember());
        // 随机取出两个数 可能两个数是同一个值
        System.out.println(opsSet.randomMembers(2));
        // 随机取出两个数 去重
        System.out.println(opsSet.distinctRandomMembers(2));
       // opsSet.pop();
        //opsSet.remove("one");
        //将集合的某个元素移动指定集合
        opsSet.move("set2","one");
        // 取出所有元素
        System.out.println(opsSet.members());
    }

    @Test
    public void testZset(){
        BoundZSetOperations<String, String> zSetOps = redisTemplate.boundZSetOps("zset");
        // 添加数据
        zSetOps.add("s1",23);
        Set<ZSetOperations.TypedTuple<String>> set = Sets.newHashSet();
        set.add(new DefaultTypedTuple<>("s2",45.0));
        set.add(new DefaultTypedTuple<>("s3",65.7));
        set.add(new DefaultTypedTuple<>("s4",55.0));
        zSetOps.add(set);
        // 获取数据个数
        System.out.println(zSetOps.zCard());
        // 指定分数的元素个数
        System.out.println(zSetOps.count(0, 60));
        System.out.println("从低到高的排名"+zSetOps.rank("s2")+1);
        System.out.println("从高到低的排名"+zSetOps.reverseRank("s2")+1);
        // 删除指定下标范围内的数据
      //  zSetOps.removeRange(0,0);
        // 删除指定元素
        //zSetOps.remove("s1");
        // 删除指定分数范围内的数据
       // zSetOps.removeRangeByScore(50,60);
        // 获取指定元素的分数
        System.out.println(zSetOps.score("s2"));
        // 获取全部数据
        System.out.println(zSetOps.range(0, -1));
        System.out.println(zSetOps.reverseRange(0, -1));
    }
}
