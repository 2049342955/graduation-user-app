/*
 * #{copyright}#
 */
package com.demo.graduationuserapp;

import com.demo.common.Constant;
import com.demo.core.message.CacheMessageSource;
import com.demo.core.web.BaseWebMvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author chenjingxiong
 */
@Configuration
public class WebMvcConfig extends BaseWebMvcConfig {

    @Autowired
    private CacheManager cacheManager;

    @Bean
    public MessageSource messageSource() {
        Cache cache = cacheManager.getCache(Constant.MESSAGE_CACHE_NAME + Constant.RDS_KEY_SEPARATOR + Constant.DEFAULT_APPLICATION_ID);
        CacheMessageSource cacheMessageSource = new CacheMessageSource(cache);
        return cacheMessageSource;
    }
}
