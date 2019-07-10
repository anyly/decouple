package com.idearfly.decouple.websocket;

import com.alibaba.fastjson.JSONObject;
import com.idearfly.decouple.service.FileService;
import com.idearfly.decouple.vo.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/wsManager")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class WsManagerController {
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
    public JSONObject put(
            HttpServletRequest request,
            @RequestParam String oldKey,
            @RequestParam String newKey,
            @RequestParam String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        return fileService.writeJSONObjectProperty(request, oldKey, newKey, jsonObject);
    }

    /**
     * 删除资源
     * @return
     */
    @DeleteMapping("/**")
    @ResponseBody
    public JSONObject delete(HttpServletRequest request, @RequestParam String key) {
        return fileService.deleteProperty(request, key);
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
        ModelAndView modelAndView = new ModelAndView("wsFile");
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
