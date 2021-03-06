package cn.yongqiang.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
@Slf4j
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.pool.max-active}")
    private Integer REDIS_POOL_MAX_ACTIVE;

    @Value("${spring.redis.pool.max-wait}")
    private Integer REDIS_POOL_MAX_WAIT;

    @Value("${spring.redis.pool.max-idle}")
    private Integer REDIS_POOL_MAX_IDLE;

    @Value("${spring.redis.pool.min-idle}")
    private Integer REDIS_POOL_MIN_IDLE;

    @Value("${spring.redis.timeout}")
    private Integer REDIS_POOL_TIMEOUT;

//    @Value("${keyCenter.config.path}")
//    private String KEY_CENTER_PATH;

//    @Value("${account.redis.connect}")
//    private String NEW_CORE_REDIS_CONNECT;

    @Value("${spring.redis.pool.testOnBorrow}")
    private boolean TEST_ON_BORROW;

    @Value("${spring.redis.pool.testWhileIdle}")
    private boolean TEST_WHILE_IDLE;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
//        KeyCenterConstants.getKeyCenterProperties(KEY_CENTER_PATH);
//        String response = KeyCenterUtil.getKeyCenterValue(NEW_CORE_REDIS_CONNECT);
//        if (StringUtils.isBlank(response)) {
//            log.error("redis?????????????????????keycenter??????redis????????????,?????????key??????{}",NEW_CORE_REDIS_CONNECT);
//            return null;
//        }
//        KeyCenterRedisConfigDTO keyCenterRedisConfigDTO = JSONObject.parseObject(response, KeyCenterRedisConfigDTO.class);

        factory.setHostName("47.94.155.21");
        factory.setDatabase(0);
        factory.setPassword("123456");
        factory.setPort(6379);
        factory.setTimeout(REDIS_POOL_TIMEOUT);
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(REDIS_POOL_MAX_ACTIVE);
        config.setMaxIdle(REDIS_POOL_MAX_IDLE);
        config.setMinIdle(REDIS_POOL_MIN_IDLE);
        config.setMaxWaitMillis(REDIS_POOL_MAX_WAIT);
        config.setTestWhileIdle(TEST_WHILE_IDLE);
        config.setTestOnBorrow(TEST_ON_BORROW);
        factory.setPoolConfig(config);


        System.out.println("redis???????????????");
        return factory;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager cacheManager =RedisCacheManager.create(redisConnectionFactory);
        return cacheManager;
    }

//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(){
//        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setConnectionFactory(jedisConnectionFactory());
//        return template;
//    }

    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key??????String??????????????????
        template.setKeySerializer(stringRedisSerializer);
        // hash???key?????????String??????????????????
        template.setHashKeySerializer(stringRedisSerializer);
        // value?????????????????????jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash???value?????????????????????jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        template.setConnectionFactory(jedisConnectionFactory());

        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory());
        stringRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return stringRedisTemplate;
    }
}

