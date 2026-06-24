package sk.upjs.nosql;

import nosql.aislike.entity.SimpleStudent;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.RedisClient;
import sk.upjs.nosql.students.SimpleStudentDao;
import tools.jackson.databind.ObjectMapper;

public enum RedisFactory {
    INSTANCE;

    public RedisClient getRedisClient() {
        return RedisClient.create("redis://localhost:6379");
    }

    public RedisClusterConfiguration redisClusterConfiguration() {
        RedisClusterConfiguration config = new RedisClusterConfiguration();
        config.addClusterNode(new RedisNode("nosql.gursky.sk", 6380));
        config.addClusterNode(new RedisNode("nosql2.gursky.sk", 6380));
        config.addClusterNode(new RedisNode("nosql3.gursky.sk", 6380));
        return config;
    }

    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory(redisClusterConfiguration());
        factory.afterPropertiesSet();
        return factory;
    }
    public RedisTemplate<String, SimpleStudent> simpleStudentTemplate() {
        RedisTemplate<String, SimpleStudent> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setDefaultSerializer(new GenericJacksonJsonRedisSerializer(new ObjectMapper()));
        template.afterPropertiesSet();
        return template;
    }
    public SimpleStudentDao simpleStudentDao() {
        return new SimpleStudentDao(simpleStudentTemplate());
    }
}
