package com.example.documentseach.common.util;

import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
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


    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            LOGGER.error("class=RedisTemplateUtils||method=incr||errMsg={}", "递增因子必须大于0");
            return null;
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            LOGGER.error("class=RedisTemplateUtils||method=decr||errMsg={}", "递减因子必须大于0");
            return null;
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /* * ========================================== Map ========================================== * */

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 全量获取hashmap中key下全量的键值对
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hmGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 批量设置hashmap下key的键值对数值
     *
     * @param key key
     * @param map key下的键值对组合
     * @return
     */
    public boolean hmSet(String key, Map<Object, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=hmSet||errMsg={}", e.getMessage());
            return false;
        }
    }

    public synchronized boolean hmSetWithExpireTime(String key, Map<Object, Object> map, Long expireTime) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (expireTime > 0) {
                expire(key, expireTime);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=hmSetWithExpireTime||errMsg={}", e.getMessage());
            return false;
        }
    }

    /**
     * 向一张hash表中插入数据，如果没有就创建
     *
     * @param key   key
     * @param item  item
     * @param value value
     * @return
     */
    public boolean hSet(String key, Object item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=hSet||errMsg={}", e.getMessage());
            return false;
        }
    }

    /**
     * 向一张hash表中插入数据，如果没有就创建,并且设置过期时间
     *
     * @param key
     * @param item
     * @param value
     * @param expireTime 设置过期时间
     * @return
     */
    public synchronized boolean hSetWithExpireTime(String key, Object item, Object value, Long expireTime) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (expireTime > 0) {
                expire(key, expireTime);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=hSetWithExpireTime||errMsg={}", e.getMessage());
            return false;
        }
    }

    /**
     * 删除hash表中对应的项目
     *
     * @param key  key
     * @param item 批量的hash表中的键值序列
     */
    public boolean hDel(String key, Object... item) {
        try {
            redisTemplate.opsForHash().delete(key, item);
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=hDel||errMsg={}", e.getMessage());
            return false;
        }
    }

    /**
     * 判断hash表中是否有该项的数值
     *
     * @param key  key
     * @param item 批量的hash表中的键值序列
     */
    public boolean hHasKey(String key, Object item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增，如果不存在就创建一个，然后将创建好的数值返回
     *
     * @param key
     * @param item
     * @param by   hash表中递增项目的数值
     * @return
     */
    public Double hIncr(String key, Object item, double by) {
        if (by < 0) {
            LOGGER.error("class=RedisTemplateUtils||method=hIncr||errMsg={}", "递增因子必须大于0");
            return null;
        }
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key
     * @param item
     * @param by   hash表中递减项目的数值
     * @return
     */
    public Double hDecr(String key, Object item, double by) {
        if (by < 0) {
            LOGGER.error("class=RedisTemplateUtils||method=hDecr||errMsg={}", "递增因子必须大于0");
            return null;
        }
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /* * ========================================== Set ========================================== * */

    /**
     * 根据key获取Set中的所有值
     *
     * @param key
     * @return key对应的set列表
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=sGet||errMsg={}", e.getMessage());
            return new HashSet<>();
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key
     * @param value
     * @return 是否存在key-value in set
     */
    public boolean sHashKey(String key, Object value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=sHashKey||errMsg={}", e.getMessage());
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key
     * @param value 数值参数列表
     * @return set缓存设置成功的个数
     */
    public Long sSet(String key, Object... value) {
        try {
            return redisTemplate.opsForSet().add(key, value);
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=sSet||errMsg={}", e.getMessage());
            return 0L;
        }
    }

    /**
     * 将数据放入set缓存,并且设置过期时间
     *
     * @param value
     * @param expireTime 过期时间
     * @param key
     * @return set缓存设置成功的个数
     */
    public synchronized Long sSetWithExpireTime(long expireTime, String key, Object... value) {
        try {
            Long addSuccess = redisTemplate.opsForSet().add(key, value);
            if (expireTime > 0) {
                expire(key, expireTime);
            }
            return addSuccess;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=sSetWithExpireTime||errMsg={}", e.getMessage());
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key
     * @return 缓存的长度
     */
    public Long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=sGetSetSize||errMsg={}", e.getMessage());
            return 0L;
        }
    }

    /**
     * 移除键值为key，数值为value的缓存数据，支持列表批量操作
     *
     * @param key
     * @param value
     * @return 移除成功的个数
     */
    public Long setRemove(String key, Object... value) {
        try {
            return redisTemplate.opsForSet().remove(key, value);
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=setRemove||errMsg={}", e.getMessage());
            return 0L;
        }
    }


    /* * ========================================== List ========================================== * */

    /**
     * 获取list缓存的内容
     *
     * @param key
     * @param start 列表索引开始位置
     * @param end   列表索引结束位置 0到-1表示全量
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=lGet||errMsg={}", e.getMessage());
            return new LinkedList<>();
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key
     * @return
     */
    public Long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=lGetListSize||errMsg={}", e.getMessage());
            return 0L;
        }
    }

    /**
     * 根据索引获取list缓存中的数值
     *
     * @param key
     * @param index 列表中的索引位置
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=lGetIndex||errMsg={}", e.getMessage());
            return null;
        }
    }

    /**
     * 将单个数据放入list缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=lSet||errMsg={}", e.getMessage());
            return false;
        }
    }


    /**
     * 将单个数据放入list缓存并且设置过期时间
     *
     * @param key
     * @param value
     * @param expireTime 过期时间设置
     * @return
     */
    public synchronized boolean lSetWithExpireTime(String key, Object value, long expireTime) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (expireTime > 0) {
                expire(key, expireTime);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=lSetWithExpireTime||errMsg={}", e.getMessage());
            return false;
        }
    }

    /**
     * 将批量数据放入list缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=lSet||errMsg={}", e.getMessage());
            return false;
        }
    }

    /**
     * 将批量数据放入list缓存并且设置过期时间
     *
     * @param key
     * @param value
     * @param expireTime 过期时间设置
     * @return
     */
    public synchronized boolean lSetWithExpireTime(String key, List<Object> value, long expireTime) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (expireTime > 0) {
                expire(key, expireTime);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=lSetWithExpireTime||errMsg={}", e.getMessage());
            return false;
        }
    }

    /**
     * 根据索引修改list缓存中的某条数据
     *
     * @param key
     * @param value
     * @param index 需要修改的索引位置
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=lUpdateIndex||errMsg={}", e.getMessage());
            return false;
        }
    }

    /**
     * 移除count个数值为value的数据
     *
     * @param key
     * @param value
     * @param count 需要移除的个数
     * @return
     */
    public Long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            LOGGER.error("class=RedisTemplateUtils||method=lRemove||errMsg={}", e.getMessage());
            return 0L;
        }
    }
}
