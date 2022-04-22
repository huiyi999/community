package com.example.community.config;

import com.alibaba.fastjson.JSON;
import com.example.community.dto.ResultDTO;
import com.example.community.exception.impl.CustomizedErrorCodeImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author : chy
 * @date: 2022-04-18 1:15 p.m.
 */

@Slf4j
public class MyAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        log.info("req.getRequestURL().toString()" + req.getRequestURL().toString());

        // check if ajax request
        boolean isAjaxRequest = false;
        if (!StringUtils.isBlank(req.getHeader("Accept")) && "application/json, text/javascript, */*; q=0.01".equals(req.getHeader("Accept"))) {
            isAjaxRequest = true;
        }

        String port = "", contextPath = "";
        if (request.getServerPort() != 80) {
            port = ":" + request.getServerPort();
        }

        StringBuilder sb = new StringBuilder();
        // http://localhost:8887/toLoginPage
        sb.append(request.getScheme()).append("://").append(request.getServerName()).append(port).append(contextPath).append("/toLoginPage");
        String loginPath = sb.toString();

        if (isAjaxRequest) {
            log.info("MyAuthenticationFilter ");
            try {
                resp.setContentType("application/json");
                resp.setStatus(200);
                resp.setCharacterEncoding("utf-8");
                // resp.setHeader("REDIRECT", "REDIRECT");
                // resp.setHeader("CONTEXTPATH", loginPath);
                PrintWriter writer = resp.getWriter();
                writer.println(JSON.toJSONString(ResultDTO.errorOf(CustomizedErrorCodeImpl.NO_LOGIN)));
                writer.close();
            } catch (IOException ioe) {

            }
        } else {
            saveRequestAndRedirectToLogin(request, response);
        }

        return false;
    }

    // @Override
    // protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
    //     HttpServletRequest req = (HttpServletRequest) request;
    //     HttpServletResponse resp = (HttpServletResponse) response;
    //     System.out.println("onLoginSuccess req.getRequestURL().toString()" + req.getRequestURL().toString());
    //     issueSuccessRedirect(request, response);
    //
    //     return false;
    // }
}

