package com.idearfly.decouple.controller.ws;

import com.alibaba.fastjson.JSONObject;
import com.idearfly.decouple.Configuration;
import com.idearfly.decouple.service.HttpService;
import com.idearfly.decouple.vo.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping(Configuration.wsManager)
@CrossOrigin
public class WsManagerController {
    @Autowired
    private HttpService httpService;

    /**
     * 重命名资源
     * @return
     */
    @PostMapping("/rename")
    @ResponseBody
    public Boolean rename(HttpServletRequest request, @RequestParam String from, @RequestParam String to) {
        return httpService.rename(request, from, to);
    }

    /**
     * 创建或修改资源
     * @return
     */
    @PutMapping("/**")
    @ResponseBody
    public JSONObject put(HttpServletRequest request, @RequestParam("data") String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        httpService.writeJSONObject(request, jsonObject);

        return jsonObject;
    }

    /**
     * 删除资源
     * @return
     */
    @DeleteMapping("/**")
    @ResponseBody
    public Boolean delete(HttpServletRequest request) {
        return httpService.delete(request);
    }

    /**
     * 读取资源
     * @return
     */
    @GetMapping("/**")
    public ModelAndView get(HttpServletRequest request) {
        String currentPath = httpService.filePath(request);
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
        JSONObject jsonObject = httpService.readJSONObject(request);
        String JSONString = "";
        if (jsonObject != null) {
            JSONString = jsonObject.toJSONString();
        }
        ModelAndView modelAndView = new ModelAndView("files");
        modelAndView.addObject("data", JSONString);
        return modelAndView;
    }


    /**
     * 目录视图
     * @param request
     * @return
     */
    private ModelAndView directory(HttpServletRequest request) {
        List<FileObject> fileObjectList = httpService.listFiles(request);
        ModelAndView modelAndView = new ModelAndView("directory");
        modelAndView.addObject("listFiles", fileObjectList);
        return modelAndView;
    }
}
