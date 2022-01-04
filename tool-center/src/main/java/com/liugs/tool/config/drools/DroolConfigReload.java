//package com.liugs.tool.config.drools;
//
//import org.kie.api.runtime.KieContainer;
//import org.springframework.beans.factory.support.DefaultListableBeanFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//
///**
// * @ClassName DroolConfigReload
// * @Description 规则配置Bean重载
// * @Author liugs
// * @Date 2021/12/6 16:23
// */
//@Configuration
//public class DroolConfigReload {
//
//    private ApplicationContext applicationContext;
//
//    public DroolConfigReload(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }
//
//    public void relaodInstance() throws IOException {
//        // 获取上下文
//        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
//
//        // 销毁指定实例
//        defaultListableBeanFactory.destroySingleton("kieContainer");
//
//        // 重新获取实例
//        KieContainer kieContainer = DroolsConfig.kieContainer();
//
//        // 重新注册同名实例
//        defaultListableBeanFactory.registerSingleton("kieContainer", kieContainer);
//    }
//}