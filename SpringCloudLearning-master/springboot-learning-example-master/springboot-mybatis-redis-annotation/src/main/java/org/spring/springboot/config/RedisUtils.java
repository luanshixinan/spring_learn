/**
 * Copyright &copy; 2015-2020 <a href="http://www.ztrk.org/">ztrk</a> All rights reserved.
 */
package org.spring.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisService
 * @Description //TODO 描述类的功能
 * @Author zhouzhendong
 * @Date 2018/6/28 15:09
 * @Version V1.0
 */
@Component
public  class RedisUtils {

    public static RedisUtils redisService;

    @Resource(name = "fastJsonRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void init() {
        redisService = this;
        redisService.redisTemplate = this.redisTemplate;
    }

    public static Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisService.redisTemplate.expire(key, timeout, unit);
    }
    public static Boolean expireAt(String key, Date date) {
        return redisService.redisTemplate.expireAt(key, date);
    }
    public static void setObject(String key, Object value) {
        redisService.redisTemplate.boundValueOps(key).set(value);
    }
    public static void setObject(String key, Object value, long timeout, TimeUnit unit) {
        redisService.redisTemplate.boundValueOps(key).set(value, timeout, unit);
    }
    public static Object getObject(String key) {
        return redisService.redisTemplate.boundValueOps(key).get();
    }
    public static Long increment(String key, long delta) {
        return redisService.redisTemplate.boundValueOps(key).increment(delta);
    }
    public static Double increment(String key, double delta) {
        return redisService.redisTemplate.boundValueOps(key).increment(delta);
    }
    public static void setMap(String key, Map value) {
        redisService.redisTemplate.boundHashOps(key).putAll(value);
    }
    public static void setMap(String key, Map value, long timeout, TimeUnit unit) {
        redisService.redisTemplate.boundHashOps(key).putAll(value);
        redisService.redisTemplate.boundHashOps(key).expire(timeout, unit);
    }
    public static Map getMap(String key) {
        return redisService.redisTemplate.boundHashOps(key).entries();
    }
    public static void setSet(String key, Set value) {
        redisService.redisTemplate.boundSetOps(key).add(new Object[] { value });
    }
    public static void setSet(String key, Set value, long timeout, TimeUnit unit) {
        redisService.redisTemplate.boundSetOps(key).add(new Object[] { value });
        redisService.redisTemplate.boundSetOps(key).expire(timeout, unit);
    }
    public static Set getSet(String key) {
        return redisService.redisTemplate.boundSetOps(key).members();
    }
    public static void setList(String key, List value) {
        redisService.redisTemplate.boundListOps(key).leftPushAll(value);
    }
    public static void setList(String key, List value, long timeout, TimeUnit unit) {
        redisService.redisTemplate.boundListOps(key).leftPushAll(value);
        redisService.redisTemplate.boundListOps(key).expire(timeout, unit);
    }
    public static List getList(String key) {
        return redisService.redisTemplate.boundListOps(key).range(0L, redisService.redisTemplate.boundListOps(key).size().longValue());
    }
    public static void del(String key) {
        redisService.redisTemplate.delete(key);
    }
    public static boolean exists(String key) {
        final byte[] keys = redisService.redisTemplate.getStringSerializer().serialize(key);
        return ((Boolean)redisService.redisTemplate.execute((RedisCallback) connection -> connection.exists(keys)
                , true)).booleanValue();
    }
}