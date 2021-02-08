package com.ymyang.framework.web.shiro;

import com.ymyang.framework.web.pojo.Permission;
import com.ymyang.framework.web.utils.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShiroUtils {

    /**
     * @return
     */
    public static Object getSubject() {
        Subject subject = SecurityUtils.getSubject();
        return subject.getPrincipal();
    }

    /**
     * 从RestController控制器中返回RequiresPermissions注解的代码
     *
     * @param basePackage
     * @return
     */
    public static List<Permission> getPermissions(String basePackage) {

        ArrayList<Permission> permissionList = new ArrayList<>();

        Set<Class> classSet = BeanUtils.scanPackage(basePackage);
        for (Class clazz : classSet) {
            Api api = (Api) clazz.getDeclaredAnnotation(Api.class);
            RestController restController = (RestController) clazz.getDeclaredAnnotation(RestController.class);
            if (restController == null) {
                continue;
            }
            Permission permission = new Permission();
            permission.setId(api.value());
            permission.setName(api.tags()[0]);
            permission.setIsNavMenu(true);
            permission.setLevel(1);
            permission.setPath(permission.getId());
            Set<Method> methods = com.ymyang.framework.beans.BeanUtils.findMethodsWithAnnotation(clazz, RequiresPermissions.class);
            if (!methods.isEmpty()) {
                permissionList.add(permission);
            }
            for (Method method : methods) {
                RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
                ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                String name = "";
                if (apiOperation != null) {
                    name = apiOperation.value();
                }
                Permission subPermission = new Permission();
                subPermission.setId(requiresPermissions.value()[0]);
                subPermission.setName(name);
                subPermission.setIsNavMenu(false);
                subPermission.setLevel(2);
                subPermission.setParentId(permission.getId());
                subPermission.setPath(permission.getId() + "|" + subPermission.getId());
                permissionList.add(subPermission);
            }
        }

        return permissionList;
    }

}
