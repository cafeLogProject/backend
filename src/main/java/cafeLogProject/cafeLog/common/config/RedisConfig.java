package cafeLogProject.cafeLog.common.config;

import cafeLogProject.cafeLog.common.auth.jwt.token.AccessToken;
import cafeLogProject.cafeLog.common.auth.jwt.token.RefreshToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Redis 호스트와 포트를 설정
        return new LettuceConnectionFactory(redisHost, redisPort);
    }


    @Bean
    public RedisTemplate<String, RefreshToken> redisTemplateRefresh(RedisConnectionFactory redisConnectionFactory) {

        return createRedisTemplate(redisConnectionFactory, RefreshToken.class);
    }

    @Bean
    public RedisTemplate<String, AccessToken> redisTemplateAccess(RedisConnectionFactory redisConnectionFactory) {

        return createRedisTemplate(redisConnectionFactory, AccessToken.class);
    }

    private <T> RedisTemplate<String, T> createRedisTemplate(RedisConnectionFactory redisConnectionFactory, Class<T> clazz) {

        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 키 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        // 값 직렬화
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(clazz);
        template.setValueSerializer(serializer);

        // 해시 키와 값 직렬화 설정
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        return template;
    }
}
