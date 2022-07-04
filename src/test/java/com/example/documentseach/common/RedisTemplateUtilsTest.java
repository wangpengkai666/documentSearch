package com.example.documentseach.common;

import com.example.documentseach.DocumentApplicationTest;
import com.example.documentseach.common.util.RedisTemplateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 对于redis模板工具类的测试用例，主要是按照redis适配的数据类型进行测试，没有单独的针对于某个api进行测试
 * @author wangpengkai
 */
public class RedisTemplateUtilsTest extends DocumentApplicationTest {

    @Autowired
    private RedisTemplateUtils redisTemplateUtils;

    @Test
    public void simpleValueTest() {
        String key = "wpk";
        String value = "test";
        // 测试普通的set get key的操作
        Assertions.assertTrue(redisTemplateUtils.set(key, value));
        Assertions.assertTrue(redisTemplateUtils.hasKey(key));
        Assertions.assertEquals(redisTemplateUtils.get(key), value);
        // 测试过期键值对时间设置操作
        Assertions.assertTrue(redisTemplateUtils.expire(key, 2L));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            return;
        }
        Assertions.assertFalse(redisTemplateUtils.hasKey(key));
        Assertions.assertTrue(redisTemplateUtils.set(key, value));
        redisTemplateUtils.delKeys(key);
        Assertions.assertFalse(redisTemplateUtils.hasKey(key));
        // 测试递增递减
        value = "1";
        Assertions.assertTrue(redisTemplateUtils.set(key, value));
        Assertions.assertEquals(Long.parseLong(value) + 1, redisTemplateUtils.incr(key, 1L));
        Assertions.assertEquals(Long.parseLong(value), redisTemplateUtils.decr(key, 1L));
        redisTemplateUtils.delKeys(key);
        Assertions.assertFalse(redisTemplateUtils.hasKey(key));
    }

    @Test
    public void mapValueTest() {

    }

    @Test
    public void setValueTest() {

    }

    @Test
    public void listValueTest() {

    }
}
