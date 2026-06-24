package sk.upjs.nosql.redischat;


import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public enum RedisConfig {
    INSTANCE;
    public static final String USERNAME = "upjs";
    public static final String PASSWORD = "dh38fhw0238923df89djkd93la9fjs0mq9gjflv9jkddj934df90rj";

    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration
                = new RedisStandaloneConfiguration("nosql.gursky.sk", 6379);
        redisStandaloneConfiguration.setUsername(USERNAME);
        redisStandaloneConfiguration.setPassword(PASSWORD);
        return redisStandaloneConfiguration;
    }

//    @Bean
//    public RedisClusterConfiguration redisClusterConfiguration() {
//        RedisClusterConfiguration config = new RedisClusterConfiguration();
//        config.addClusterNode(new RedisNode("nosql.gursky.sk", 6380));
//        config.addClusterNode(new RedisNode("nosql2.gursky.sk", 6380));
//        config.addClusterNode(new RedisNode("nosql3.gursky.sk", 6380));
//        return config;
//    }

    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration());
        factory.afterPropertiesSet();
        return factory;
    }
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setDefaultSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
    public RedisConnection redisConnection() {
        return redisConnectionFactory().getConnection();
    }
}
