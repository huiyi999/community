package com.example.community.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.codec.Base64;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        factoryBean.setLoginUrl("/toLoginPage");
        factoryBean.setSuccessUrl("/index");
        factoryBean.setUnauthorizedUrl("/toLoginPage");

        // Priority of the interceptor: from top to bottom, from left to right
        // LinkedHashMap is ordered
        Map<String, String> filterChainMap = new LinkedHashMap<>();

        //  anyone can access, eg: sign in/up or static resources
        filterChainMap.put("/css/**", "anon");
        filterChainMap.put("/images/**", "anon");
        filterChainMap.put("/js/**", "anon");

        filterChainMap.put("/toLoginPage", "anon");
        filterChainMap.put("/toRegisterPage", "anon");

        // logged in users can access, including remember me, if use /** = authc，RememberMe not work
        filterChainMap.put("/profile/**", "user");
        // filterChainMap.put("/post/**", "user");
        filterChainMap.put("/publish/**", "authc");
        filterChainMap.put("/comment/**", "myAuthc");
        filterChainMap.put("/likeComment", "myAuthc");
        filterChainMap.put("/notification/**", "user");

        // filterChainMap.put("/comment/**", "authc");
        // filterChainMap.put("/comment", "user");

        // must be logged in to access, excluding remember me
        // start with “/user/admin” 开头的用户需要身份认证，authc 表示要进行身份认证
        // filterChainMap.put("/user/admin/**", "authc");
        // specify filtering rules, for /user/admin/  only has permission: user:[*] can access, or specify user:xxx
        // filterChainMap.put("/user/admin/**", "perms[user:*]");

        // set logout filter
        filterChainMap.put("/logout", "logout");

        // 设置自定义 filter
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("anyRoleFilter", new MyRolesAuthorizationFilter());
        filterMap.put("myAuthc", new MyAuthenticationFilter());
        factoryBean.setFilters(filterMap);

        // store map into bean
        factoryBean.setFilterChainDefinitionMap(filterChainMap);
        return factoryBean;

    }

    @Bean
    public UserRealm myShiroRealm() {
        return new UserRealm();
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // connect Realm
        securityManager.setRealm(myShiroRealm());
        // add rememberMeManager
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //  30 days, seconds
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("pybbs is the best!".getBytes()));
        return cookieRememberMeManager;

    }


    @Bean
    public FormAuthenticationFilter formAuthenticationFilter() {
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        //corresponding to HTML checkbox name = rememberMe
        formAuthenticationFilter.setRememberMeParam("rememberMe");
        return formAuthenticationFilter;
    }

    /**
     * integrate thymeleaf
     */
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

}
