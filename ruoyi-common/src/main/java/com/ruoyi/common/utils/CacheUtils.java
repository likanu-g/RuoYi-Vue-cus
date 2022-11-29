package com.ruoyi.common.utils;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.util.concurrent.TimeUnit;

/**
 * 缓存处理
 *
 * @author ruoyi
 */
public class CacheUtils {
    /**
     * 设置缓存
     *
     * @param key      键
     * @param value    值
     * @param timeout  超时时间
     * @param timeUnit 时间单位
     */
    public static void putCache(String key, Object value, Integer timeout, TimeUnit timeUnit) {
        if (!RuoYiConfig.isEhCacheEnabled()) {
            SpringUtils.getBean(RedisCache.class).setCacheObject(key, value, timeout, timeUnit);
        } else {
            //TODO 重复提交有5000ms的有效期
            //TODO 令牌有30min的有效期
            //TODO 验证码有2min的有效期
            //TODO 密码锁定时间有10min的有效期
            Cache cache = SpringUtils.getBean(CacheManager.class).getCache(Constants.DEFAULT_EHCACHE);
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
    public static void putCache(String key, Object value) {
        if (!RuoYiConfig.isEhCacheEnabled()) {
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
    public static <T> T getCache(String key) {
        if (!RuoYiConfig.isEhCacheEnabled()) {
            return SpringUtils.getBean(RedisCache.class).getCacheObject(key);
        } else {
            Cache cache = SpringUtils.getBean(CacheManager.class).getCache(Constants.DEFAULT_EHCACHE);
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
    public static void deleteCache(String key) {
        if (!RuoYiConfig.isEhCacheEnabled()) {
            SpringUtils.getBean(RedisCache.class).deleteObject(key);
        } else {
            Cache cache = SpringUtils.getBean(CacheManager.class).getCache(Constants.DEFAULT_EHCACHE);
            cache.remove(key);
        }
    }
}
