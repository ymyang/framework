package com.ymyang.framework.web.exceptionhandler;

import com.ymyang.framework.beans.ResponseEntity;
import com.ymyang.framework.web.constant.ExceptionConstant;
import com.ymyang.framework.web.pojo.ValidError;
import com.ymyang.framework.web.validation.AtLeastOneNotEmpty;
import com.ymyang.framework.web.validation.AtLeastOneNotEmptyValidator;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintValidator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class BindValidControllerAdvice {

    /**
     * 自定义验证规则
     * Map：key为自定义验证注解名，
     * value验证的类型为类 @Constraint.validatedBy，
     * 且需实现静态方法 public static ValidError getError(ObjectError objectError)
     */
    private static Map<String, Class<? extends ConstraintValidator>> codeValidatorMap = new HashMap<>();

    static {
        codeValidatorMap.put(AtLeastOneNotEmpty.class.getSimpleName(), AtLeastOneNotEmptyValidator.class);
    }

    /**
     * 表单验证错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ArrayList<ValidError>> handlerBindException(Exception e) {

        ArrayList<ValidError> errorList = new ArrayList<>();

        List<FieldError> fieldErrors;
        if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            fieldErrors = bindException.getFieldErrors();
            if (fieldErrors.isEmpty()) {
                List<ObjectError> allErrors = bindException.getBindingResult().getAllErrors();
                for (ObjectError objectError : allErrors) {
                    Class validatorClazz = codeValidatorMap.get(objectError.getCode());
                    if (validatorClazz == null) {
                        continue;
                    }
                    try {
                        Method handlerError = validatorClazz.getDeclaredMethod("getError", ObjectError.class);
                        errorList.add((ValidError) handlerError.invoke(null, objectError));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                        ValidError validError = new ValidError();
                        validError.setMessage(objectError.getDefaultMessage());
                        validError.setCode(objectError.getCode());
                        validError.setField(objectError.getObjectName());
                        errorList.add(validError);
                    }

                }
            }
        } else {
            fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
        }

        fieldErrors.forEach(error -> {
            int index = error.getDefaultMessage().lastIndexOf(ExceptionConstant.SEPRATER);
            ValidError validError = new ValidError();
            validError.setMessage(index > 0 ? error.getDefaultMessage().substring(index + ExceptionConstant.SEPRATER.length()) : error.getDefaultMessage());
            validError.setCode(error.getCode());
            validError.setField(error.getField());
            validError.setRejectedValue(error.getRejectedValue());
            errorList.add(validError);
        });
        return ResponseEntity.error(errorList.get(0).getMessage(), errorList);

    }

}
