package cafeLogProject.cafeLog.auth.jwt.token;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshRepository {

    private final RedisTemplate<String, RefreshToken> redisTemplate;

    public RefreshRepository(RedisTemplate<String, RefreshToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void deleteByUsername(String username) {
        redisTemplate.delete("refresh:" + username);
    }

    public void save(RefreshToken refreshToken) {
        redisTemplate.opsForValue().set("refresh:" + refreshToken.getUsername(), refreshToken);
    }

    public RefreshToken findByUsername(String username) {
        return redisTemplate.opsForValue().get("refresh:" + username);
    }
}
