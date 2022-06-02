package com.example.community.controller;

import com.example.community.model.User;
import com.example.community.service.NotificationService;
import com.example.community.service.UserService;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.ui.Model;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/login")
    public String login(@RequestParam(value = "username", required = false) String username,
                        @RequestParam(value = "password", required = false) String password,
                        @RequestParam(defaultValue = "0", required = false) Boolean rememberMe,
                        Model model,
                        HttpServletRequest request) {
        System.out.println("login username:" + username);
        System.out.println("rememberMe:" + rememberMe);

        String msg = null;

        if (username != null && password != null) {

            // get subject for Authenticated
            Subject subject = SecurityUtils.getSubject();
            // Session session = subject.getSession(true);

            // SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            // System.out.println("savedRequest: " + savedRequest);
            if (!subject.isAuthenticated()) {

                try {
                    UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);

                    // start Authentication，turn to UserRealm
                    subject.login(token);

                    // manually write into session
                    User user = (User) SecurityUtils.getSubject().getPrincipal();
                    request.getSession().setAttribute("user", user);
                    Long unreadCount = notificationService.unreadCount(user.getId());
                    request.getSession().setAttribute("unreadCount", unreadCount);

                    // automatically write into cookie
                    // response.addCookie(new Cookie("token", token.toString()));
                    // if(savedRequest != null){
                    //     System.out.println("before login url: "+savedRequest);
                    //     response.sendRedirect(savedRequest.getRequestUrl());
                    // }

                    return "redirect:/";
                } catch (UnknownAccountException e) {
                    e.printStackTrace();
                    msg = "User not exist，error：" + e.getMessage();
                } catch (IncorrectCredentialsException e) {
                    e.printStackTrace();
                    msg = "Incorrect username or password，error：" + e.getMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                    msg = "Unknown error，error：" + e.getMessage();
                }
            }
        } else {
            msg = "Please input username and password!";
        }
        model.addAttribute("msg", msg);
        // failed to sign in, redirect to login page
        return "user/login";
    }

    // /**
    //  * 退出登录，我们不需要实现，Shiro的Filter过滤器会帮我们生成一个logout请求，
    //  * 当浏览器访问`/logout`请求时，Shiro会自动清空缓存并重定向到配置好的loginUrl页面
    //  *
    //  * @return
    //  */
    // @GetMapping("/logout")
    // public void logout() {
    //     SecurityUtils.getSubject().logout();
    // }

    @PostMapping("/register")
    public String register(@RequestParam(value = "username", required = false) String username,
                           @RequestParam(value = "password", required = false) String password,
                           Model model) {
        System.out.println(username);
        String msg = null;

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRoleId(Long.valueOf(3));
        user.setAvatarUrl("/images/user-profile.png");

        int i = userService.addUser(user);
        System.out.println(i);
        if (i == 1) {
            msg = "Sign up Successfully! Please login in!";
            model.addAttribute("msg", msg);
            return "user/login";
        } else {
            msg = "Sign up failed! User already exists!";
            model.addAttribute("msg", msg);
            return "user/register";
        }
    }
}

