package com.idearfly.decouple.controller.http;

import com.idearfly.decouple.Configuration;
import com.idearfly.decouple.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(Configuration.httpApi)
@CrossOrigin
public class HttpApiController {
    @Autowired
    private HttpService httpService;

    /**
     * 读取数据
     * @return
     */
    @GetMapping("/**")
    @ResponseBody
    public String getData(HttpServletRequest request) {
        return httpService.readContent(request);
    }


}
