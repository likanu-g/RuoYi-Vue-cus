package com.ruoyi.common.utils;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存处理
 *
 * @author ligaoyuan
 */
public class CacheUtils {
    //在线用户缓存keys
    private static Set<String> tokenCacheKeys = new HashSet<>();
    //参数配置缓存keys
    private static Set<String> sysConfigCacheKeys = new HashSet<>();

    /**
     * 设置缓存
     *
     * @param key      键
     * @param value    值
     * @param timeout  超时时间
     * @param timeUnit 时间单位
     * @param cacheName ehCache的缓存名称                
     */
    public static void putCacheObject(String key, Object value, Integer timeout, TimeUnit timeUnit, String cacheName) {
        if (!RuoYiConfig.isEhCacheEnabled()) {
            SpringUtils.getBean(RedisCache.class).setCacheObject(key, value, timeout, timeUnit);
        } else {
            //重复提交有5000ms的有效期
            //令牌有30min的有效期
            //验证码有2min的有效期
            //密码锁定时间有10min的有效期
            Cache cache = SpringUtils.getBean(CacheManager.class).getCache(cacheName);
            Element element = new Element(key, value);
            cache.put(element);
        }
    }


    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     */
    public static void putCacheObject(String key, Object value) {
        putCacheObject(key, value, RuoYiConfig.isEhCacheEnabled());
    }

    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     * @param ehCacheEnabled 是否开启ehCache缓存             
     */
    public static void putCacheObject(String key, Object value, boolean ehCacheEnabled) {
        if (!ehCacheEnabled) {
            SpringUtils.getBean(RedisCache.class).setCacheObject(key, value);
        } else {
            Cache cache = SpringUtils.getBean(CacheManager.class).getCache(Constants.DEFAULT_EHCACHE);
            Element element = new Element(key, value);
            cache.put(element);
        }
    }

    /**
     * 获取缓存
     *
     * @param key 键
     */
    public static <T> T getCacheObject(String key) {
        return getCacheObject(key, Constants.DEFAULT_EHCACHE);
    }

    /**
     * 获取缓存
     *
     * @param key 键
     */
    public static <T> T getCacheObject(String key, String cacheName) {
        if (!RuoYiConfig.isEhCacheEnabled()) {
            return SpringUtils.getBean(RedisCache.class).getCacheObject(key);
        } else {
            Cache cache = SpringUtils.getBean(CacheManager.class).getCache(cacheName);
            Element element = cache.get(key);
            if (element != null) {
                return (T) element.getObjectValue();
            }
        }

        return null;
    }


    /**
     * 删除缓存
     *
     * @param key 键
     */
    public static void deleteCacheObject(String key) {
        if (!RuoYiConfig.isEhCacheEnabled()) {
            SpringUtils.getBean(RedisCache.class).deleteObject(key);
        } else {
            Cache cache = SpringUtils.getBean(CacheManager.class).getCache(Constants.DEFAULT_EHCACHE);
            cache.remove(key);
        }
    }

    /**
     * 增加在线用户缓存key
     * @param key 键值
     */
    public static void addTokenCacheKey(String key) {
        tokenCacheKeys.add(key);
    }

    /**
     * 删除在线用户缓存key
     * @param key 键值
     */
    public static void removeTokenCacheKey(String key) {
        tokenCacheKeys.remove(key);
    }

    /**
     * 获取在线用户缓存keys
     */
    public static Set<String> getTokenCacheKeys() {
        return tokenCacheKeys;
    }

    /**
     * 增加参数配置缓存key
     * @param key 键值
     */
    public static void addConfigCacheKey(String key) {
        sysConfigCacheKeys.add(key);
    }

    /**
     * 删除参数配置缓存key
     * @param key 键值
     */
    public static void removeConfigCacheKey(String key) {
        sysConfigCacheKeys.remove(key);
    }

    /**
     * 获取参数配置缓存keys
     */
    public static Set<String> getConfigCacheKeys() {
        return sysConfigCacheKeys;
    }
}
