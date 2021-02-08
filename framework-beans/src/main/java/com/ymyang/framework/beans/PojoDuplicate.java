package com.ymyang.framework.beans;

import com.ymyang.framework.beans.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 被实体类实现后，可实现属性复制，功能似 @Link BeanUtils.copyProperties
 *
 * @Link BeanUtils.copyProperties
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface PojoDuplicate extends Serializable {

    // 如果当前类名修改，需修改此处
    Class<PojoDuplicate> baseModelClass = PojoDuplicate.class;

    /**
     * 从source生成entity
     */
    default void copyPropertiesFrom(Object source, boolean ignoreBlankString) {

        Class<?> targetClass = this.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(targetClass);
//        PropertyDescriptor[] sourcePds = getPropertyDescriptors(source.getClass());

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }
            Class<?> propertyType = targetPd.getPropertyType();

            PropertyDescriptor sourcePd = null;
            try {
                sourcePd = new PropertyDescriptor(targetPd.getName(), source.getClass());
            } catch (IntrospectionException e) {
                continue;
            }
            if (sourcePd == null) {
                continue;
            }
            Method readMethod = sourcePd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            try {
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }

                Object sourceValue = readMethod.invoke(source);
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                // 原值为null时，直接拷贝(保证下面的集合、自定义类型操作不会抛出NEP)
                if (sourceValue == null) {
//                    writeMethod.invoke(this, sourceValue);
                    continue;
                }
                // 如果是字符串
                if (String.class.isAssignableFrom(sourceValue.getClass())) {
                    if (sourceValue.toString().trim().equals("") && ignoreBlankString) {
//                        writeMethod.invoke(this, null);
                    } else {
                        writeMethod.invoke(this, sourceValue);
                    }
                    continue;
                }

                // 如果是：实体属性是枚举
                // Object -> Enum
                if (Enum.class.isAssignableFrom(targetPd.getPropertyType())
                        && !Enum.class.isAssignableFrom(sourcePd.getPropertyType())) {
                    Object[] enumConstants = targetPd.getPropertyType().getEnumConstants();
                    Method propertyMethod = targetPd.getPropertyType().getMethod("getByValue", Serializable.class);
                    if (propertyMethod == null) {
                        throw new BusinessException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target," +
                                        " 枚举类型必须实现静态方法 getByValue() ");
                    }
                    writeMethod.invoke(this, propertyMethod.invoke(enumConstants[0], sourceValue));
                    continue;
                }
                // Enum -> Object
                if (Enum.class.isAssignableFrom(sourcePd.getPropertyType())
                        && !Enum.class.isAssignableFrom(targetPd.getPropertyType())) {
                    Method propertyMethod = sourcePd.getPropertyType().getMethod("getValue");
                    if (propertyMethod == null) {
                        throw new BusinessException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target, " +
                                        " 枚举类型必须实现成员方法 getValue() ");
                    }
                    writeMethod.invoke(this, propertyMethod.invoke(sourceValue));
                    continue;
                }

                if (Boolean.class.isAssignableFrom(sourcePd.getPropertyType())
                        && Number.class.isAssignableFrom(targetPd.getPropertyType())) {
                    writeMethod.invoke(this, ((Boolean) sourceValue) ? 1 : 0);
                    continue;
                }
                if (Boolean.class.isAssignableFrom(targetPd.getPropertyType())
                        && Number.class.isAssignableFrom(sourcePd.getPropertyType())) {
                    writeMethod.invoke(this, sourceValue.equals(0) ? false : true);
                    continue;
                }

                // 如果是：集合
                if (Collection.class.isAssignableFrom(sourcePd.getPropertyType())) {
                    if (!List.class.isAssignableFrom(sourcePd.getPropertyType())) {
                        throw new BusinessException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target," +
                                        " target type only support ArrayList");
                    }
                    List sourceValueList = (List) sourceValue;

                    // 空时，直接copy
                    if (sourceValueList.size() == 0) {
                        writeMethod.invoke(this, sourceValue);
                        continue;
                    }

                    Method targetPdReadMethod = targetPd.getReadMethod();

                    // 获取集合的泛型
                    Class clazz = null;
                    try {
                        ParameterizedType genericReturnType = (ParameterizedType) targetPdReadMethod.getGenericReturnType();
                        clazz = (Class<?>)genericReturnType.getActualTypeArguments()[0];
                    } catch (Exception e) {
                    }

                    Object targetPdValue = targetPdReadMethod.invoke(this);
                    if (clazz == null && targetPdValue == null) {
                        writeMethod.invoke(this, sourceValue);
                        continue;
                    }

                    // 不是EntityDuplicate类型，没必要遍历再去copyProperties，直接复制。
                    if (!baseModelClass.isAssignableFrom(clazz)) {
                        writeMethod.invoke(this, sourceValue);
                        continue;
                    }

                    List targetValueList = new ArrayList(sourceValueList.size());
                    writeMethod.invoke(this, targetValueList);
                    for (Object sourceVal : sourceValueList) {
                        Object newTargetVal = clazz.newInstance();//BeanUtils.instantiateClass(clazz);
                        targetValueList.add(newTargetVal);
                        ((PojoDuplicate) newTargetVal).copyPropertiesFrom(sourceVal, ignoreBlankString);
                    }
                    continue;
                }

                // 如果是：自定义类型
                if (baseModelClass.isAssignableFrom(targetPd.getPropertyType())) {
                    Method targetReadMethod = targetPd.getReadMethod();
                    Object targetPdValue = targetReadMethod.invoke(this);
                    if (targetPdValue == null) {
                        targetPdValue = targetPd.getPropertyType().newInstance();
                        //BeanUtils.instantiateClass(targetPd.getPropertyType());
                        writeMethod.invoke(this, targetPdValue);
                    }
                    ((PojoDuplicate) targetPdValue).copyPropertiesFrom(sourceValue, ignoreBlankString);
                    continue;
                }

                // 如果是：Java内置类型
                writeMethod.invoke(this, sourceValue);
            } catch (Throwable ex) {
                throw new BusinessException(
                        "Could not copy property '" + targetPd.getName() + "' from source to target, " + ex.getMessage(), ex);
            }
        }
    }


    /**
     * 将entity转换为 target output
     *
     * @param target
     */
    default void copyPropertiesTo(Object target, boolean ignoreBlankString) {
        Class<?> targetClass = target.getClass();

        PropertyDescriptor[] targetPds = getPropertyDescriptors(targetClass);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }
            PropertyDescriptor sourcePd = null;
            try {
                sourcePd = new PropertyDescriptor(targetPd.getName(), this.getClass());
            } catch (IntrospectionException e) {
                continue;
            }
            if (sourcePd == null) {
                continue;
            }
            Method readMethod = sourcePd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            try {
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }

                Object sourceValue = readMethod.invoke(this);
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                // 原值为null时，直接拷贝(保证下面的集合、自定义类型操作不会抛出NEP)
                if (sourceValue == null) {
//                    writeMethod.invoke(target, sourceValue);
                    continue;
                }


                if (Boolean.class.isAssignableFrom(sourcePd.getPropertyType())
                        && Number.class.isAssignableFrom(targetPd.getPropertyType())) {
                    writeMethod.invoke(target, ((Boolean) sourceValue) ? 1 : 0);
                    continue;
                }
                if (Boolean.class.isAssignableFrom(targetPd.getPropertyType())
                        && Number.class.isAssignableFrom(sourcePd.getPropertyType())) {
                    writeMethod.invoke(target, sourceValue.equals(0) ? false : true);
                    continue;
                }

                // 如果是字符串
                if (String.class.isAssignableFrom(sourceValue.getClass())) {
                    if (sourceValue.toString().trim().equals("") && ignoreBlankString) {
//                        writeMethod.invoke(this, null);
                    } else {
                        writeMethod.invoke(target, sourceValue);
                    }
                    continue;
                }

                // 如果是：实体属性是枚举
                // Object -> Enum
                if (Enum.class.isAssignableFrom(targetPd.getPropertyType())
                        && !Enum.class.isAssignableFrom(sourcePd.getPropertyType())) {
                    Object[] enumConstants = targetPd.getPropertyType().getEnumConstants();
                    Method propertyMethod = targetPd.getPropertyType().getMethod("getByValue", Serializable.class);
                    if (propertyMethod == null) {
                        throw new BusinessException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target," +
                                        " 枚举类型必须实现静态方法getByValue() ");
                    }
                    writeMethod.invoke(target, propertyMethod.invoke(enumConstants[0], sourceValue));
                    continue;
                }
                // Enum -> Object
                if (Enum.class.isAssignableFrom(sourcePd.getPropertyType())
                        && !Enum.class.isAssignableFrom(targetPd.getPropertyType())) {
                    Method propertyMethod = sourcePd.getPropertyType().getMethod("getValue");
                    if (propertyMethod == null) {
                        throw new BusinessException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target, " +
                                        " 枚举类型必须实现成员方法getValue() ");
                    }
                    writeMethod.invoke(target, propertyMethod.invoke(sourceValue));
                    continue;
                }

                // 如果是：集合
                if (Collection.class.isAssignableFrom(sourcePd.getPropertyType())) {
                    if (!List.class.isAssignableFrom(sourcePd.getPropertyType())) {
                        throw new BusinessException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target," +
                                        " target type only support ArrayList.");
                    }
                    List sourceValueList = (List) sourceValue;
                    if (sourceValueList.size() == 0) {
                        writeMethod.invoke(target, sourceValue);
                        continue;
                    }

                    // 不是EntityDuplicate类型，没必要遍历再去copyProperties，直接复制。
                    if (!baseModelClass.isAssignableFrom(sourceValueList.get(0).getClass())) {
                        writeMethod.invoke(target, sourceValue);
                        continue;
                    }

                    // 获取集合的泛型
                    Class clazz = null;
                    try {
                        ParameterizedType genericReturnType = (ParameterizedType) sourcePd.getReadMethod().getGenericReturnType();
                        clazz = (Class<?>)genericReturnType.getActualTypeArguments()[0];
                    } catch (Exception e) {
                    }

                    Method targetPdReadMethod = targetPd.getReadMethod();
                    Object targetPdValue = targetPdReadMethod.invoke(target);
                    if (clazz == null && targetPdValue == null) {
                        writeMethod.invoke(target, sourceValue);
                        continue;
                    }

                    List targetValueList = new ArrayList();
                    writeMethod.invoke(target, targetValueList);
                    for (Object source : sourceValueList) {
                        Object newTargetVal = clazz.newInstance();//BeanUtils.instantiateClass(clazz);
                        targetValueList.add(newTargetVal);
                        ((PojoDuplicate) source).copyPropertiesTo(newTargetVal, ignoreBlankString);
                    }
                    continue;
                }
                // 如果是：自定义类型
                if (baseModelClass.isAssignableFrom(sourceValue.getClass())) {
                    Method targetReadMethod = targetPd.getReadMethod();
                    Object targetPdValue = targetReadMethod.invoke(target);
                    if (targetPdValue == null) {
                        targetPdValue = targetPd.getPropertyType().newInstance();
                        //BeanUtils.instantiateClass(targetPd.getPropertyType());
                        writeMethod.invoke(target, targetPdValue);
                    }
                    ((PojoDuplicate) sourceValue).copyPropertiesTo(targetPdValue, ignoreBlankString);
                    continue;
                }
                // 如果是：Java内置类型
                writeMethod.invoke(target, sourceValue);
            } catch (Throwable ex) {
                throw new BusinessException(
                        "Could not copy property '" + targetPd.getName() + "' from source to target, " + ex.getMessage(), ex);
            }


        }
    }

    /**
     * Retrieve the JavaBeans {@code PropertyDescriptor}s of a given class.
     *
     * @param clazz the Class to retrieve the PropertyDescriptors for
     * @return an array of {@code PropertyDescriptors} for the given class
     */
    default PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
//        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> allFieldsList = BeanUtils.getAllFieldsList(clazz);

        List<PropertyDescriptor> propertyDescriptorsList = new ArrayList<>();
        for (Field field : allFieldsList) {
            try {
                propertyDescriptorsList.add(new PropertyDescriptor(field.getName(), clazz));
            } catch (Exception e) {
            }
        }
        return propertyDescriptorsList.toArray(new PropertyDescriptor[propertyDescriptorsList.size()]);
    }


}
