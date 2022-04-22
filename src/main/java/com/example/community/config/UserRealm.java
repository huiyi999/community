package com.example.community.config;


import com.example.community.model.Permission;
import com.example.community.model.Role;
import com.example.community.model.User;
import com.example.community.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // get username from Authorization
        String username = (String) principalCollection.getPrimaryPrincipal();
        System.out.println("doGetAuthorizationInfo username: " + username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        Role role = userService.queryRoleByUsername(username);

        List<Permission> permissions = userService.queryPermissionByUsername(username);

        info.addRole(role.getName());
        for (Permission permission : permissions) {
            info.addStringPermission(permission.getName());
        }
        return info;

    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // System.out.println(token.getPrincipal());
        // System.out.println(token.getUsername());
        // System.out.println(token.getPassword());

        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());

        System.out.println("doGetAuthenticationInfo username:" + username);
        User user = userService.queryUserByUsername(username);

        if (user == null)
            throw new UnknownAccountException();

        if (!password.equals(user.getPassword()))
            throw new IncorrectCredentialsException();

        // store into Session
        SecurityUtils.getSubject().getSession().setAttribute("user", user);
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
}
