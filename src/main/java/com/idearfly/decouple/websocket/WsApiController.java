package com.idearfly.decouple.websocket;

import com.alibaba.fastjson.JSONObject;
import com.idearfly.decouple.DecoupleConfiguration;
import com.idearfly.decouple.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/wsRoom")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class WsApiController {
    @Autowired
    private FileService fileService;

    /**
     * 读取数据
     * @return
     */
    @RequestMapping("/**")
    @ResponseBody
    public ModelAndView entry(
            HttpServletRequest request,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String usecase) {
        String servletPath = request.getServletPath();
        String path = servletPath.replaceAll("^/wsRoom", "/wsManager");
        path = fileService.filePath(path);
        if (usecase==null) {
            return hall(path, servletPath);
        } else {
            return room(path, name, usecase);
        }
    }

    private ModelAndView hall(String path, String servletPath) {
        JSONObject jsonObject = fileService.readJSONObject(path);
        String topic = servletPath.replaceAll("^/wsRoom", "");
        Map<String, Set<String>> usecaseMap = WebSocketHandler.TopicMap.getOrDefault(topic, new LinkedHashMap<>());

        ModelAndView modelAndView = new ModelAndView("wsRoom");
        modelAndView.addObject("data", jsonObject);
        modelAndView.addObject("usecaseMap", usecaseMap);
        return modelAndView;
    }

    private ModelAndView room(String path, String name, String usecase) {
        JSONObject jsonObject = fileService.readJSONObject(path);
        ModelAndView modelAndView = new ModelAndView("entryRoom");
        modelAndView.addObject("data", jsonObject);
        modelAndView.addObject("name", name);
        modelAndView.addObject("usecase", usecase);
        return modelAndView;
    }
}
