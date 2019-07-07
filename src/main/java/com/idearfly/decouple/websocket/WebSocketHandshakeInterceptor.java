package com.idearfly.decouple.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();

            String name = httpServletRequest.getParameter("name");
            if (name == null) {
                return false;
            }
            String topic = httpServletRequest.getServletPath();
            topic = topic.replaceAll("^/ws", "");
            String usecase = httpServletRequest.getParameter("usecase");
            if (usecase == null) {
                return false;
            }
            String avatar = httpServletRequest.getParameter("avatar");

            attributes.put("name", name);
            attributes.put("topic", topic);
            attributes.put("usecase", usecase);
            if (avatar != null) {
                attributes.putIfAbsent("avatar", avatar);
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
