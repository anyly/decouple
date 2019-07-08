package com.idearfly.decouple;

import com.alibaba.fastjson.JSON;
import com.idearfly.decouple.vo.FileSupport;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@org.springframework.context.annotation.Configuration
public class DecoupleConfiguration implements WebMvcConfigurer {
    @Resource(name="thymeleafViewResolver")
    private ThymeleafViewResolver thymeleafViewResolver;

    public static final String httpManager = "/manager";

    public static final String httpApi = "/api";

    public static Map<String, FileSupport> FileSupport = new HashMap<>();

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        if (thymeleafViewResolver != null) {
            Map<String, Object> vars = new HashMap<>(8);
            vars.put("httpManager", httpManager);
            vars.put("httpApi", httpApi);
            vars.put("fileSupport", FileSupport);
            thymeleafViewResolver.setStaticVariables(vars);
        }
    }
}
