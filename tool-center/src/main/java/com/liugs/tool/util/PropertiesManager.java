package com.liugs.tool.util;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @ClassName PropertiesManager
 * @Description 配置文件key
 * @Author liugs
 * @Date 2020/9/8 14:08:41
 */
@Slf4j
@Component
public class PropertiesManager {

    @Autowired
    private Environment environment;

    private Properties properties = new Properties();

    public Properties getProperties() {
        initProperties();
        return properties;
    }

    @PostConstruct
    public void initProperties() {
        if (environment instanceof AbstractEnvironment) {
            Set<String> keySet = getKeySet();
            for (String key : keySet) {
                //从enviroment中获取所有属性，缓存到properties中
                if (!StringUtils.isEmpty(environment.getProperty(key))) {
                    properties.setProperty(key, environment.getProperty(key));
                }
            }
            log.info("EsToolProperties属性填充完成，一共填充了：{} 个", properties.size());
        } else {
            log.info("EsToolProperties属性填充失败");
            System.exit(0);
        }

    }

    public static  Set<String> getKeySet() {
        Set<String> keySet = new HashSet<>(16);
        Map<String, Field> map = ReflectUtil.getFieldMap(PropertiesManager.class);
        Iterator<Map.Entry<String, Field>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Field> entry = iterator.next();
            Field field = entry.getValue();
            try {
                keySet.add((String) field.get(entry.getKey()));
            } catch (Exception e) {
                System.out.println("从ToolPropertiesKey中获取配置文件key异常：" + e);
            }
        }
        return keySet;
    }
}
