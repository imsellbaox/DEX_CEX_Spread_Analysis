package com.dex.dex.config;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class MybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
//        return new PaginationInterceptor();
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //TODO: mybatis-plus do not support TDengine, use postgresql Dialect
        paginationInterceptor.setDialectType("postgresql");
        return paginationInterceptor;
    }

}
