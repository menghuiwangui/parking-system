package com.example.campus_parking_system.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    // 向 Spring 容器注入一个名为 mybatisPlusInterceptor 的拦截器 Bean
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 创建插件管理器实例
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 核心：往管理器中添加“分页内部拦截器”，并指定数据库类型为 MySQL
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return mybatisPlusInterceptor;
    }
}
