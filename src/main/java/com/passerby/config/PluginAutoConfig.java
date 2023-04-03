package com.passerby.config;

import com.passerby.plugin.AutoIdInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PluginAutoConfig {

    /**
     * 注入拦截器
     * @return
     */
    @Bean
    public Interceptor autoIdInterceptor() {
        return new AutoIdInterceptor();
    }
}
