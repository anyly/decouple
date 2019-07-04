package com.idearfly.decouple.controller;

import com.idearfly.decouple.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private FileService fileService;

    /**
     * 读取数据
     * @return
     */
    @GetMapping("/**")
    @ResponseBody
    public String getData(HttpServletRequest request) {
        return fileService.readContent(request);
    }


}
