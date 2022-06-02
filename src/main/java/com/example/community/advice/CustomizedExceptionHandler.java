package com.example.community.advice;

import com.example.community.dto.ResultDTO;
import com.example.community.exception.CustomizedException;
import com.example.community.exception.impl.CustomizedErrorCodeImpl;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : chy
 * @date: 2022-04-16 7:42 p.m.
 */
@ControllerAdvice
public class CustomizedExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    Object handle(HttpServletRequest request, HttpServletResponse response, Throwable ex, Model model) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            /* return json */
            if (ex instanceof CustomizedException) {
                return ResultDTO.errorOf((CustomizedException) ex);
            } else {
                return ResultDTO.errorOf(CustomizedErrorCodeImpl.SYS_ERROR);
            }
            // try {
            //     response.setContentType("application/json");
            //     response.setStatus(200);
            //     response.setCharacterEncoding("utf-8");
            //     PrintWriter writer = response.getWriter();
            //     // writer.write(JSON.toJSONString(resultDTO));
            //     writer.close();
            // } catch (IOException ioe) {
            // }
            // return null;
        } else {
            // redirect to error page
            if (ex instanceof CustomizedException) {
                model.addAttribute("message", ex.getMessage());
            } else {
                // unhandled exception CustomizedErrorController
                model.addAttribute("message", CustomizedErrorCodeImpl.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}
