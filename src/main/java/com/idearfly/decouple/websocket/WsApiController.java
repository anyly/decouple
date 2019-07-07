package com.idearfly.decouple.websocket;

import com.alibaba.fastjson.JSONObject;
import com.idearfly.decouple.DecoupleConfiguration;
import com.idearfly.decouple.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ws")
@CrossOrigin
public class WsApiController {
    @Autowired
    private FileService fileService;

    /**
     * 读取数据
     * @return
     */
    @GetMapping("/**")
    @ResponseBody
    public ModelAndView entry(HttpServletRequest request,
                             @RequestParam(required = false) String room) {
        String servletPath = request.getServletPath();
        servletPath = servletPath.replaceAll("^/ws", "/wsManager");
        String path = fileService.filePath(servletPath);
        if (room==null) {
            return hall(path);
        } else {
            return room(path, room);
        }
    }

    private ModelAndView hall(String path) {
        JSONObject jsonObject = fileService.readJSONObject(path);
        ModelAndView modelAndView = new ModelAndView("wsRoom");
        modelAndView.addObject("data", jsonObject);
        return modelAndView;
    }

    private ModelAndView room(String path, String room) {
        JSONObject jsonObject = fileService.readJSONObject(path);
        ModelAndView modelAndView = new ModelAndView("entryRoom");
        modelAndView.addObject("data", jsonObject);
        return modelAndView;
    }
}
