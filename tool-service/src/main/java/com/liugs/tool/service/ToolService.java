package com.liugs.tool.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName ToolService
 * @Description
 * @Author liugs
 * @Date 2021/2/27 17:39:30
 */
@SpringBootApplication(scanBasePackages = {"com.liugs.tool.*","com.tydic.rule.*"})
//@EnableDiscoveryClient
//@EnableCircuitBreaker
//@EnableHystrix
@MapperScan(basePackages = {"com.liugs.tool.dao","com.tydic.rule.dao"})
public class ToolService {

    public static void main(String[] args) {

        SpringApplication.run(ToolService.class, args);
    }
}
