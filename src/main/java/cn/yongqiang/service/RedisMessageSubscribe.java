package cn.yongqiang.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisMessageSubscribe implements MessageListener {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
        String msg= redisSerializer.deserialize(message.getBody());
        System.out.println("接收到的消息是："+ msg);
        log.info("Received <" + msg + ">");
    }
}
