package com.example.taskapp.Config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String TASK_CACHE = "taskCache";
    public static final String SUBTASK_CACHE = "subtaskCache";
    public static final String COLUMN_CACHE = "columnCache";
    public static final String BOARD_CACHE = "boardCache";

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                buildCache(TASK_CACHE, 10),
                buildCache(SUBTASK_CACHE, 5),
                buildCache(COLUMN_CACHE, 30),
                buildCache(BOARD_CACHE, 5)
        ));
        return cacheManager;
    }

    private CaffeineCache buildCache(String name, long duration) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(duration, TimeUnit.MINUTES)
                .build());
    }
}