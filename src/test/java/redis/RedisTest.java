package redis;

import cn.yongqiang.PracticeApplication;
import cn.yongqiang.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PracticeApplication.class)
public class RedisTest {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void contextLoads() {
        // 可以传一个对象json
        redisTemplate.opsForValue().set("myKey","北京市昌平区");
        System.out.println("获取的值是："+redisTemplate.opsForValue().get("myKey"));
    }

    @Test
    public void publishMsgTest(){
        try {
            redisTemplate.convertAndSend("whitelist", "消息变动通知");
            log.info("消息发送成功");
        }catch (Exception e) {
            e.printStackTrace();
            log.error("消息发送失败");
        }
    }

    @Test
    public void useHash(){
        Map<String,Object> map = new HashMap<>();
        map.put("aaa","1551090");
        map.put("bbb","1345788");
        map.put("ccc","1567888");
        redisUtil.hmset("whitelist", map);

        log.info("map为：{}", redisUtil.hmget("whitelist"));
        log.info("boolean为：{}", redisUtil.hHasKey("whitelist", "aba"));
    }
}
