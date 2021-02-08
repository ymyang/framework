package com.ymyang.framework.mybatisplus.injector;

import com.ymyang.framework.mybatisplus.annotation.InnerJoin;
import com.ymyang.framework.mybatisplus.annotation.InnerJoins;
import com.ymyang.framework.mybatisplus.annotation.LeftJoin;
import com.ymyang.framework.mybatisplus.annotation.LeftJoins;
import com.ymyang.framework.mybatisplus.injector.methods.JoinsMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@Component
public class JoinDaoSupport implements BeanPostProcessor, PriorityOrdered {


    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (MapperFactoryBean.class.isAssignableFrom(bean.getClass())) {
            addJoinsMapper(((MapperFactoryBean) bean));
            return bean;
        }
        return bean;
    }

    private void addJoinsMapper(MapperFactoryBean mapperFactoryBean) {
        MapperBuilderAssistant mapperBuilderAssistant = new MapperBuilderAssistant(
                mapperFactoryBean.getSqlSession().getConfiguration(),
                mapperFactoryBean.getObjectType().getName()
        );
        mapperBuilderAssistant.setCurrentNamespace(mapperFactoryBean.getObjectType().getName());
        Map<Method, List<Annotation>> joinsMethods = lookupJoins(mapperFactoryBean);
        JoinsMethod joinsMethod = new JoinsMethod();
        for (Map.Entry<Method, List<Annotation>> entry : joinsMethods.entrySet()) {
            ParameterizedType parameterizedType = (ParameterizedType) mapperFactoryBean.getObjectType().getGenericInterfaces()[0];
            Method method = entry.getKey();
            joinsMethod.injectJoinsMappedStatement(
                    mapperBuilderAssistant, method.getDeclaringClass(),
                    (Class<?>) parameterizedType.getActualTypeArguments()[0],
                    this.getReturnType(method), entry.getValue(), method.getName()
            );
        }

    }


    /**
     * 查找MapperFactoryBean中有Join注解的方法，并包装在Map中
     *
     * @param mapperFactoryBean
     * @return
     */
    private Map<Method, List<Annotation>> lookupJoins(MapperFactoryBean mapperFactoryBean) {
        Method[] methods = mapperFactoryBean.getMapperInterface().getMethods();
        Map<Method, List<Annotation>> joinMethodAnnotationMap = new HashMap<>();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            List<Annotation> joinAnnotations = new ArrayList<>();
            for (Annotation annotation : annotations) {
                if (annotation instanceof LeftJoin || annotation instanceof InnerJoin) {
                    joinAnnotations.add(annotation);
                }
                if (annotation instanceof InnerJoins) {
                    joinAnnotations.addAll(Arrays.asList(((InnerJoins) annotation).value()));
                }
                if (annotation instanceof LeftJoins) {
                    joinAnnotations.addAll(Arrays.asList(((LeftJoins) annotation).value()));
                }
            }
            if (!joinAnnotations.isEmpty()) {
                joinAnnotations.sort((one, two) -> {
                    int order1 = 0;
                    int order2 = 0;
                    if (one instanceof LeftJoin) {
                        order1 = ((LeftJoin) one).order();
                    }
                    if (one instanceof InnerJoin) {
                        order1 = ((InnerJoin) one).order();
                    }
                    if (two instanceof LeftJoin) {
                        order2 = ((LeftJoin) two).order();
                    }
                    if (two instanceof InnerJoin) {
                        order2 = ((InnerJoin) two).order();
                    }
                    return order1 - order2;
                });
                joinMethodAnnotationMap.put(method, joinAnnotations);
            }
        }
        return joinMethodAnnotationMap;
    }

    /**
     * 获得方法的返回类型，如果是集合则返回泛型
     *
     * @param method
     * @return
     */
    private Class<?> getReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        if (Collection.class.isAssignableFrom(returnType) ||
                IPage.class.isAssignableFrom(returnType) ||
                Optional.class.isAssignableFrom(returnType)
        ) {
            ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
            return ((Class<?>) parameterizedType.getActualTypeArguments()[0]);
        }
        return returnType;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
