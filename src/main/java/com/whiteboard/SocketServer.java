package com.whiteboard;


import cn.hutool.core.util.StrUtil;
import com.whiteboard.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerEndpoint("/whiteboard/{userId}")
@Component
@Slf4j
public class SocketServer {

    private StringRedisTemplate redisTemplate;

    private static final String key = "COOR";

    private static Map<String, Session> userMap = new HashMap<>();

    private String userId;


    /**
     * 打开连接时触发事件
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws IOException {
        if (this.redisTemplate == null) {
            redisTemplate = SpringUtils.getBean(StringRedisTemplate.class);
        }
        this.userId = userId;
        if (!userMap.containsKey(userId)) {
            List<String> range = redisTemplate.opsForList().range(key, 0, -1);
            for (String s : range) {
                session.getBasicRemote().sendText(s);
            }
        }
        userMap.put(userId, session);
        log.info("用户" + userId + "已上线");
    }

    /**
     * 收到消息时触发事件
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
        log.info("用户" + userId + "发来的消息为 ：" + message);

        redisTemplate.opsForList().leftPush(key, message);
        for (Map.Entry<String, Session> entry : userMap.entrySet()) {
            if (!StrUtil.equals(session.getRequestParameterMap().get("userId").get(0), entry.getKey())) {
                entry.getValue().getBasicRemote().sendText(message);
            }
        }
    }

    /**
     * 关闭连接时触发事件
     */
    @OnClose
    public void onClose() {
        userMap.remove(userId);
        log.info("用户" + userId + "已下线");
    }

    /**
     * 传输消息错误时触发事件
     */
    @OnError
    public void onError(Throwable error) {
        log.error("错误发生");
        error.printStackTrace();
    }

}
