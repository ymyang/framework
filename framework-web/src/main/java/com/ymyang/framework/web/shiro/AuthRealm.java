package com.ymyang.framework.web.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;


public class AuthRealm extends AuthorizingRealm {

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     * <p>
     * 自动缓存角色和权限，变更权限和角色要清理缓存
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        JWTUser jwtUser = ((JWTUser) principals.getPrimaryPrincipal());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 用户角色
        String role = "root";
        // 每个角色拥有默认的权限
        String rolePermission = "root";
        // 每个用户可以设置新的权限
        Set<String> roleSet = new HashSet<>();
        roleSet.add(role);
        Set<String> permissionSet = new HashSet<>();
        permissionSet.add("*");
        //需要将 role, permission 封装到 Set 作为 info.setRoles(), info.setStringPermissions() 的参数
        permissionSet.add(rolePermission);
        //设置该用户拥有的角色和权限
        authorizationInfo.setRoles(roleSet);
        authorizationInfo.setStringPermissions(permissionSet);
        return authorizationInfo;

    }


    /**
     * 默认使用此方法进行TOKEN，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String token = (String) authenticationToken.getCredentials();

        JWTUser jwtUser = JWTUtil.getJWTUser(token);
        if (jwtUser == null || !JWTUtil.verify(token, jwtUser)) {
            throw new AuthenticationException("认证失败[Authorization无效或过期]！");
        }

        return new SimpleAuthenticationInfo(jwtUser, token, "MyRealm");

    }

}
