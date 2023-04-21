package com.traderepublic.application.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("candlesticks");
    }

    @CacheEvict(allEntries = true, value = {"candlesticks"})
    @Scheduled(fixedDelay = 40, timeUnit = TimeUnit.SECONDS, initialDelay = 500)
    public void reportCacheEvict() {
        log.info("Cache flushed {}", LocalDateTime.now());
    }

}
