package com.idearfly.decouple.controller;

import com.alibaba.fastjson.JSONObject;
import com.idearfly.decouple.DecoupleConfiguration;
import com.idearfly.decouple.service.FileService;
import com.idearfly.decouple.vo.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping(DecoupleConfiguration.httpManager)
@CrossOrigin(origins = "*", allowCredentials = "true")
public class ManagerController {
    @Autowired
    private FileService fileService;

    /**
     * 重命名资源
     * @return
     */
    @PostMapping("/rename")
    @ResponseBody
    public Boolean rename(HttpServletRequest request, @RequestParam String from, @RequestParam String to) {
        return fileService.rename(request, from, to);
    }

    /**
     * 创建或修改资源
     * @return
     */
    @PutMapping("/**")
    @ResponseBody
    public JSONObject put(HttpServletRequest request, @RequestParam("data") String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        fileService.writeJSONObject(request, jsonObject);

        return jsonObject;
    }

    /**
     * 删除资源
     * @return
     */
    @DeleteMapping("/**")
    @ResponseBody
    public Boolean delete(HttpServletRequest request) {
        return fileService.delete(request);
    }

    /**
     * 读取资源
     * @return
     */
    @GetMapping("/**")
    public ModelAndView get(HttpServletRequest request) {
        String currentPath = fileService.filePath(request);
        File currentFile = new File(currentPath);
        if (currentFile.isDirectory()) {
            return directory(currentFile);
        } else{
            return file(currentFile);
        }
    }

    /**
     * 文件视图
     * @param file
     * @return
     */
    private ModelAndView file(File file) {
        JSONObject jsonObject = fileService.readJSONObject(file);
        ModelAndView modelAndView = new ModelAndView("file");
        modelAndView.addObject("data", jsonObject);
        return modelAndView;
    }


    /**
     * 目录视图
     * @param file
     * @return
     */
    private ModelAndView directory(File file) {
        List<FileObject> fileObjectList = fileService.listFiles(file);
        ModelAndView modelAndView = new ModelAndView("directory");
        modelAndView.addObject("listFiles", fileObjectList);
        return modelAndView;
    }
}
