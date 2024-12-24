package cafeLogProject.cafeLog.config;

import cafeLogProject.cafeLog.auth.jwt.AccessToken;
import cafeLogProject.cafeLog.auth.jwt.RefreshToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

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
