package com.example.community.controller;

import com.example.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : chy
 * @date: 2022-04-21 2:23 p.m.
 */

@Controller
public class FileController {

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");

        FileDTO fileDTO = new FileDTO();
        try {
            fileDTO.setSuccess(1);
            // uncompleted

            return fileDTO;
        } catch (Exception e) {

            fileDTO.setSuccess(0);
            fileDTO.setMessage("Upload failed");
            return fileDTO;
        }
    }
}
