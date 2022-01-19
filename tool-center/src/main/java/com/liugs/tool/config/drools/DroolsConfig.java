package com.liugs.tool.config.drools;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * @ClassName DroolsConfig
 * @Description
 * @Author liugs
 * @Date 2021/9/8 16:56:01
 */
@Slf4j
@Configuration
public class DroolsConfig {

    /** 指定规则文件存放的目录 */
    private static final String RULES_PATH = "rules/";

    private final KieServices kieServices = KieServices.Factory.get();


    /**
     * 远端规则jar
     * @return
     * @throws IOException
     */
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

    @Bean
    @ConditionalOnMissingBean
    public KieFileSystem kieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] files = resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "*.*");
        String path;
        for (Resource file : files) {
            path = RULES_PATH + file.getFilename();
            kieFileSystem.write(ResourceFactory.newClassPathResource(path, "UTF-8"));
        }
        return kieFileSystem;
    }

    @Bean("kieContainer")
    @ConditionalOnMissingBean(KieContainer.class)
    public KieContainer kieContainer() throws IOException {
        KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem());
        kieBuilder.buildAll();
        return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }

    @Bean("kieBase")
    @ConditionalOnMissingBean(KieBase.class)
    public KieBase kieBase() throws IOException {
        KieBase kieBase = kieContainer().getKieBase();
        return kieBase;
    }

    @Bean
    @ConditionalOnMissingBean
    public KModuleBeanFactoryPostProcessor kiePostProcessor() {
        return new KModuleBeanFactoryPostProcessor();
    }
}
