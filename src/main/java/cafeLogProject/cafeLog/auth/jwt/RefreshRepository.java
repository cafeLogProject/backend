package cafeLogProject.cafeLog.auth.jwt;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshRepository {

    private final RedisTemplate<String, RefreshToken> redisTemplate;

    public RefreshRepository(RedisTemplate<String, RefreshToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean existsByUsername(String username) {
        return redisTemplate.opsForValue().get(username) != null;
    }

    public void deleteByUsername(String username) {
        redisTemplate.delete(username);
    }

    public void save(RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(refreshToken.getUsername(), refreshToken);
    }

    public RefreshToken findByUsername(String username) {
        return redisTemplate.opsForValue().get(username);
    }
}
