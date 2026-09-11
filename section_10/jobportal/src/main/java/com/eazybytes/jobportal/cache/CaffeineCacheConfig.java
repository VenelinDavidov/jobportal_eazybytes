package com.eazybytes.jobportal.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfig {

    @Value("${cache.jobs.ttl-minutes:5}")
    private int jobsTtlMinutes;

    @Value("${cache.jobs.max-size:2000}")
    private int jobsCacheMaxSize;

    @Value ("${cache.companies.ttl-minutes:5}")
    private int companiesTtlMinutes;

    @Value ("${cache.companies.max-size:100}")
    private int companiesCacheMaxSize;

    @Value ("${cache.roles.ttl-days:2}")
    private int rolesTtlDays;

    @Value ("${cache.roles.max-size:50}")
    private int rolesCacheMaxSize;



    @Bean
    public CacheManager caffeineCacheManager() {

        CaffeineCache jobsCash = new CaffeineCache("jobs",
                Caffeine.newBuilder()
                        .expireAfterWrite(jobsTtlMinutes, TimeUnit.MINUTES)
                        .maximumSize(jobsCacheMaxSize)
                        .build());

        CaffeineCache companiesCache = new CaffeineCache("companies",
                Caffeine.newBuilder()
                        .expireAfterWrite(companiesTtlMinutes, TimeUnit.MINUTES)
                        .maximumSize(companiesCacheMaxSize)
                        .build());

        CaffeineCache rolesCache = new CaffeineCache("roles",
                Caffeine.newBuilder()
                        .expireAfterWrite(rolesTtlDays, TimeUnit.DAYS)
                        .maximumSize(rolesCacheMaxSize)
                        .build());

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(jobsCash, companiesCache, rolesCache));

        return cacheManager;
    }
}
