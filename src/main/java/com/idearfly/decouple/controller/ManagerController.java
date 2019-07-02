package com.idearfly.decouple.controller;

import com.alibaba.fastjson.JSONObject;
import com.idearfly.decouple.service.FileService;
import com.idearfly.decouple.vo.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private FileService fileService;

    /**
     * 修改数据
     * @return
     */
    @PutMapping("/**")
    @ResponseBody
    public JSONObject putData(HttpServletRequest request, @RequestParam("data") String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        fileService.writeJSONObject(request, jsonObject);

        return jsonObject;
    }

    /**
     * 删除数据
     * @return
     */
    @DeleteMapping("/**")
    @ResponseBody
    public Boolean deleteData(HttpServletRequest request) {
        return fileService.deleteJSONObject(request);
    }

    /**
     * 读取数据
     * @return
     */
    @GetMapping("/**")
    public ModelAndView getData(HttpServletRequest request) {
        String currentPath = fileService.filePath(request);
        File currentFile = new File(currentPath);
        if (currentFile.isDirectory()) {
            return directory(request);
        } else{
            return file(request);
        }
    }

    /**
     * 文件视图
     * @param request
     * @return
     */
    private ModelAndView file(HttpServletRequest request) {
        JSONObject jsonObject = fileService.readJSONObject(request);
        ModelAndView modelAndView = new ModelAndView("manager");
        modelAndView.addObject("data", jsonObject);
        return modelAndView;
    }


    /**
     * 目录视图
     * @param request
     * @return
     */
    private ModelAndView directory(HttpServletRequest request) {
        List<FileObject> fileObjectList = fileService.listFiles(request);
        ModelAndView modelAndView = new ModelAndView("directory");
        modelAndView.addObject("listFiles", fileObjectList);
        return modelAndView;
    }
}
