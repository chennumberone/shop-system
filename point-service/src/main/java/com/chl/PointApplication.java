package com.chl;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@MapperScan(value = "com.chl.dao")
@SpringBootApplication
@EnableDubboConfiguration
public class PointApplication
{
    public static void main(String[] args )
    {
        SpringApplication.run(PointApplication.class,args);
    }
}
