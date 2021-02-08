package com.ymyang.framework.web.validation;

import com.ymyang.framework.web.pojo.ValidError;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * @See AtLeastOneNotEmpty 自定义的注解
 * String：注解参数类型
 */
public class AtLeastOneNotEmptyValidator implements ConstraintValidator<AtLeastOneNotEmpty, Object> {

    private String[] fields;

    //1、初始化方法：通过该方法我们可以拿到我们的注解
    @Override
    public void initialize(AtLeastOneNotEmpty constraintAnnotation) {
        fields = constraintAnnotation.value();
    }

    //2、逻辑处理
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        try {
            for (String fieldName : fields) {
                Object property = getField(value, fieldName);
                if (!StringUtils.isEmpty(property)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private Object getField(Object object, String fieldName) throws IllegalAccessException {
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                field.setAccessible(true);
                return field.get(object);
            }
        }
        return null;
    }

    public static ValidError getError(ObjectError objectError) {
        String[] argument = (String[]) objectError.getArguments()[1];

        ValidError validError = new ValidError();
        validError.setMessage(objectError.getDefaultMessage());
        validError.setCode(objectError.getCode());
        validError.setField(argument[0]);
        return validError;
    }
}
