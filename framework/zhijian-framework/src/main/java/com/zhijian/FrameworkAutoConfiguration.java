package com.zhijian;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

//@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class ,MybatisAutoConfiguration.class})
//@ComponentScan
//@Configuration
//@EnableAutoConfiguration
@Configuration
// @EnableAutoConfiguration(exclude = MybatisAutoConfiguration.class)
@ComponentScan(basePackages = "com.zhijian", excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = FrameworkAutoConfiguration.class))
public class FrameworkAutoConfiguration {
}
