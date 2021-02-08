package com.ymyang.framework.web.utils;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.util.HashSet;
import java.util.Set;

public class BeanUtils {

    /**
     * 扫描包
     *
     * @param basePackage
     * @return
     */
    public static Set<Class> scanPackage(String basePackage) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
        Set<BeanDefinition> components = provider.findCandidateComponents(basePackage);
        HashSet<Class> clazz = new HashSet();
        components.forEach((component) -> {
            String beanClassName = component.getBeanClassName();
            try {
                Class<?> beanClass = Class.forName(beanClassName);
                clazz.add(beanClass);
            } catch (ClassNotFoundException e) {
            }
        });
        return clazz;
    }

}
