package com.cx.tool;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;


/**
 * 需要生成一个唯一的16位的订单号，订单号的规则是:一共是16位的数字,
 * 前面8位是日期比如2021年09月01就是20210901,后面8位是是自增。并且每天redis要清0，第二天又重0，1开始。
 */
@Component
public class OrderIdUtil {
    //订单缓存key
    private static final String ORDER_KEY = "ORDER_KEY";
    //初始值
    private static final String ORDER_INIT = "0";
    //时间格式
    private static final String DATA_FORMAT = "yyyyMMdd";
    //订单最大长度
    private static final int MAX_LEN = 8;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    private String generateId(String key) {
        BoundValueOperations<String, String> valueOps = stringRedisTemplate.boundValueOps(key);
        String value = valueOps.get();
        if (StringUtils.isEmpty(value)) {
            valueOps.set(ORDER_INIT);
        }
        Long orderId = valueOps.increment(1);
        if(Long.valueOf(1).equals(orderId)){
            valueOps.expireAt(getExpireDate());
        }
        int len = String.valueOf(orderId).length();
        //不到8位补零
        StringBuffer stringBuffer = new StringBuffer();
        if (len < 8) {
            for (int i = 0; i < (MAX_LEN - len); i++) {
                stringBuffer.append("0");
            }
            stringBuffer.append(orderId);
        }
        return stringBuffer.toString();
    }

    public String genOrderId(){
        String id = generateId(ORDER_KEY);
        String orderId = formatId(id,DATA_FORMAT);
        return orderId;
    }

    private String formatId(String id, String dataFormat) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getDateFormat(DATA_FORMAT));
        stringBuffer.append(id);
        return stringBuffer.toString();
    }

    private String getDateFormat(String dataFormat) {
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern(Strings.isBlank(dataFormat) ? DATA_FORMAT : dataFormat);
        return df.format(new Date());
    }


    private Date getExpireDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

}
