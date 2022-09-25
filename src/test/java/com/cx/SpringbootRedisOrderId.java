package com.cx;

import com.cx.tool.OrderIdUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringbootRedisOrderId {
    @Autowired
    private OrderIdUtil orderIdUtil;

    @Test
    public void test() throws InterruptedException {
        for(int i =0;i<100;i++){
            final int j = i;
            new Thread(()->{
                System.out.println((j+1)+"号订单好："+orderIdUtil.genOrderId());
            }).start();
        }

        TimeUnit.SECONDS.sleep(20);
    }
}
