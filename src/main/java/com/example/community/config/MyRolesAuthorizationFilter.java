package com.example.community.config;

import lombok.SneakyThrows;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class MyRolesAuthorizationFilter extends AuthorizationFilter {
    @SuppressWarnings({"unchecked"})
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;
        if (rolesArray == null || rolesArray.length == 0) {
            //no roles specified, so nothing to check - allow access.
            return false;
        }
        List<String> roles = CollectionUtils.asList(rolesArray);
        boolean[] hasRoles = subject.hasRoles(roles);
        for (boolean hasRole : hasRoles) {
            if (hasRole) {
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        System.out.println("onAccessDenied");
        Subject subject = getSubject(request, response);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // If the subject isn't identified, redirect to login URL
        if (subject.getPrincipal() == null) {
//            saveRequestAndRedirectToLogin(request, response);
            System.out.println("no login user");
            String objectStr= "{'name':'no login user'}";
            JSONObject jsonObject=new JSONObject(objectStr);
            PrintWriter wirte = null;
            wirte = response.getWriter();
            wirte.print(jsonObject);
        } else {
            System.out.println("no permission");
            String objectStr= "{'name':'no permission'}";
            JSONObject jsonObject=new JSONObject(objectStr);
            PrintWriter wirte = null;
            wirte = response.getWriter();
            wirte.print(jsonObject);
        }

        return false;
    }
}
