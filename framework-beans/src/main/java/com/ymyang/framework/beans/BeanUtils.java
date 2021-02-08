package com.ymyang.framework.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BeanUtils {

    /**
     * 获取含有注解annotation的方法
     *
     * @param clazz
     * @return
     */
    public static <T extends Annotation> Set<Method> findMethodsWithAnnotation(Class clazz, Class<T> annotation) {
        Method[] methods = clazz.getDeclaredMethods();
        HashSet<Method> methodSet = new HashSet();
        for (Method method : methods) {
            T foundAnnotation = method.getAnnotation(annotation);
            if (foundAnnotation != null) {
                methodSet.add(method);
            }
        }
        return methodSet;
    }

    /**
     * 获取所有继承而来的属性 Class.getDeclaredFields()
     * @param cls
     * @return
     */
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                allFields.add(field);
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }


}
