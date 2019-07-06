package com.idearfly.decouple;

import lombok.Value;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@org.springframework.context.annotation.Configuration
public class Configuration implements WebMvcConfigurer {
    @Resource(name="thymeleafViewResolver")
    private ThymeleafViewResolver thymeleafViewResolver;

    public static final String httpManager = "/manager";

    public static final String httpApi = "/http";

    public static final String wsManager = "/wsManager";

    public static final String wsApi = "/ws";

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        if (thymeleafViewResolver != null) {
            Map<String, Object> vars = new HashMap<>(8);
            vars.put("httpManager", httpManager);
            vars.put("httpApi", httpApi);
            vars.put("wsManager", wsManager);
            vars.put("wsApi", wsApi);
            thymeleafViewResolver.setStaticVariables(vars);
        }
    }
}
