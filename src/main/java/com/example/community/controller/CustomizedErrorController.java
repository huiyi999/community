package com.example.community.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The CustomizedErrorController picks up any unhandled exceptions (unCustomized Exception).
 * like: 500, 404, 403, 401
 *
 * @author : chy
 * @date: 2022-04-16 8:58 p.m.
 */

@Controller("/error")
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomizedErrorController implements ErrorController {

    public final static String STATUS_CODE = "javax.servlet.error.status_code";

    /**
     * Handle html Page request error
     */
    @RequestMapping(value = "/error", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, Model model) throws IOException {

        HttpStatus status = getStatus(request);

        // request error
        if (status.is4xxClientError()) {
            model.addAttribute("message", "Wrong request!");
        }

        // server error
        if (status.is5xxServerError()) {
            model.addAttribute("message", "System wrong, please try again later.");
        }

        return new ModelAndView("error");
    }

    /**
     * obtain HTTP Request status code
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute(STATUS_CODE);
        /* Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE); */
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
