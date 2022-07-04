package com.example.documentseach.common.util;

import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author wangpengkai
 */
@Component
public class RedisTemplateUtils {
    private static final KLog LOGGER = LoggerFactory.getLog(RedisTemplateUtils.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key        key
     * @param expireTime 过期时间
     */
    public boolean expire(String key, Long expireTime) {
        try {
            if (expireTime > 0) {
                redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=expire||expire fail||key={}||errMsg={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 根据key获取其过期时间
     *
     * @param key key
     * @return 返回0表示永久有效
     */
    public Long getExpireTimeByKey(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key key
     * @return
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 删除缓存
     *
     * @param key key
     */
    public void delKeys(String... key) {
        try {
            redisTemplate.delete(Arrays.asList(key));
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=delKeys||del fail||keys={}", (Object) key);
        }
    }

    /* * ========================================== String ========================================== * */

    /**
     * 普通缓存获取
     *
     * @param key key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   key
     * @param value value
     * @return
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=set||set key value fail||key={}||value={}", key, value);
            return false;
        }
    }

    /**
     * 普通缓存放入并且设置过期时间
     *
     * @param key        key
     * @param value      value
     * @param expireTime 过期时间
     * @return
     */
    public boolean setWithExpireTime(String key, Object value, Long expireTime) {
        try {
            if (expireTime > 0) {
                redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=setWithExpireTime||set key value fail||key={}||value={}", key, value);
            return false;
        }
    }
}
