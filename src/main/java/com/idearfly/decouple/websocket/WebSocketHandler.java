package com.idearfly.decouple.websocket;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class WebSocketHandler extends TextWebSocketHandler {
    /**
     * 用户信息
     * Map { name : avatar }
     */
    public static final Map<String, String> UserMap = new LinkedHashMap<>();
    /**
     * 客户端会话信息
     * Map { client : clientSession }
     */
    public static final Map<String, WebSocketSession> ClientMap = new LinkedHashMap<>();
    /**
     * 客户以某个用例来参与的业务主题
     * Map { topic : {  usecase : [ client ] } }
     */
    public static final Map<String, Map<String, Set<String>>> TopicMap = new LinkedHashMap<>();

    /**
     * 连接成功后：
     * 1. 加入ClientMap
     * 2. 维护UserMap
     * 3. 获取topic，并加入相应的主题
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        String name = (String) attributes.getOrDefault("name", "");
        String avatar = (String) attributes.getOrDefault("avatar", "/images/avatar.svg");
        String topic = (String) attributes.getOrDefault("topic", "");
        String usecase = (String) attributes.getOrDefault("usecase", "default");

        ClientMap.put(name, session);
        UserMap.put(name, avatar);

        Map<String, Set<String>> usecaseMap = TopicMap.computeIfAbsent(topic, k -> new LinkedHashMap<>());
        Set<String> userOfTopic = usecaseMap.computeIfAbsent(usecase, k -> new HashSet<>());
        userOfTopic.add(name);

        super.afterConnectionEstablished(session);
    }

    /**
     * 断开连接后：
     * 1. 从ClientMap中移除
     * 2. 从UserMap中移除
     * 3. 从每一个业务主题下移除
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        String name = (String) attributes.getOrDefault("name", "");
        String avatar = (String) attributes.getOrDefault("avatar", "/images/avatar.svg");
        String topic = (String) attributes.getOrDefault("topic", "");
        String usecase = (String) attributes.getOrDefault("usecase", "");

        ClientMap.remove(name);
        UserMap.remove(name);
        try {
            TopicMap.get(topic).get(usecase).remove(name);
        } catch (Exception e) {

        }
        super.afterConnectionClosed(session, status);
    }

    /**
     * 收到消息转发给除了自己以外的所有人
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (!pingPong(session, message)) {
            return;
        }
        Map<String, Object> attributes = session.getAttributes();
        String name = (String) attributes.getOrDefault("name", "");
        String topic = (String) attributes.getOrDefault("topic", "");
        String usecase = (String) attributes.getOrDefault("usecase", "");
        try {
            System.out.println("up>>>>["+name+"]>>>>["+message.getPayload().toString()+"]");
            Set<String> userOfTopic = TopicMap.get(topic).get(usecase);
            for (String user: userOfTopic) {
                if (!user.equals(name)) {
                    ClientMap.get(user).sendMessage(message);
                    System.out.println("down>>>>["+user+"]>>>>["+message.getPayload().toString()+"]");
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        super.handleTextMessage(session, message);
    }

    private boolean pingPong(
            WebSocketSession session,
            TextMessage message) throws IOException {
        Map<String, Object> attributes = session.getAttributes();
        String name = (String) attributes.getOrDefault("name", "");
        JSONObject json = JSONObject.parseObject(message.getPayload());
        String type = json.getString("type");
        if (type != null && type.equals("heartbeatReq")) {
            //System.out.println("ping>>>>["+name+"]>>>>["+message.getPayload().toString()+"]");
            json.put("type", "heartbeatRes");
            TextMessage textMessage = new TextMessage(json.toJSONString());
            session.sendMessage(textMessage);
            //System.out.println("pong>>>>["+name+"]>>>>["+textMessage.getPayload().toString()+"]");
            return false;
        }
        return true;
    }
}
