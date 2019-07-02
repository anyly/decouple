package com.idearfly.decouple.controller;

import com.idearfly.decouple.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/data")
public class DataController {
    @Autowired
    private FileService fileService;

    /**
     * 读取数据
     * @return
     */
    @GetMapping("/**")
    public String getData(HttpServletRequest request) {
        String path = request.getServletPath().replaceAll("^/data", "/files");
        return path;
    }


}
