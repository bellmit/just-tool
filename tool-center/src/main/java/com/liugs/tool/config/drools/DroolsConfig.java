//package com.liugs.tool.config.drools;
//
//import lombok.extern.slf4j.Slf4j;
//import org.kie.api.KieBase;
//import org.kie.api.KieServices;
//import org.kie.api.builder.KieModule;
//import org.kie.api.builder.KieRepository;
//import org.kie.api.io.Resource;
//import org.kie.api.runtime.KieContainer;
//import org.kie.spring.KModuleBeanFactoryPostProcessor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * @ClassName DroolsConfig
// * @Description
// * @Author liugs
// * @Date 2021/9/8 16:56:01
// */
//@Slf4j
//@Configuration
//public class DroolsConfig {
//
//    private static final KieServices kieServices = KieServices.Factory.get();
//
//
//    @Bean("kieContainer")
//    public static KieContainer kieContainer() throws IOException {
//
//        String url = "https://oss-cn-beijing.aliyuncs.com/tool-api-1.0.0.jar?OSSAccessKeyId=&Expires=1631180524&Signature=naxbczgwtyNfYDWcOaH7Qv4FEmw%3D";
//
//        Resource resource = kieServices.getResources().newUrlResource(url);
//        InputStream inputStream = resource.getInputStream();
//
//        KieRepository repository = kieServices.getRepository();
//
//        KieModule kieModule = repository.addKieModule(kieServices.getResources().newInputStreamResource(inputStream));
//
//        return kieServices.newKieContainer(kieModule.getReleaseId());
//    }
//
//    @Bean
//    public KieBase kieBase() throws IOException {
//        KieBase kieBase = kieContainer().getKieBase();
//        return kieBase;
//    }
//
//    @Bean
//    public KModuleBeanFactoryPostProcessor kiePostProcessor() {
//        return new KModuleBeanFactoryPostProcessor();
//    }
//}
