package com.idearfly.decouple.controller;

import com.idearfly.decouple.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@CrossOrigin
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
