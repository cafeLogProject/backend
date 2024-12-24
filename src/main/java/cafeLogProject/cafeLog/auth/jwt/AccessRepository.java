package cafeLogProject.cafeLog.auth.jwt;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccessRepository {

    private final RedisTemplate<String, AccessToken> redisTemplate;

    public AccessRepository(RedisTemplate<String, AccessToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean existsByUsername(String username) {
        return redisTemplate.opsForValue().get(username) != null;
    }

    public void deleteByUsername(String username) {
        redisTemplate.delete(username);
    }

    public void save(AccessToken accessToken) {
        redisTemplate.opsForValue().set(accessToken.getUsername(), accessToken);
    }

    public AccessToken findByUsername(String username) {
        return redisTemplate.opsForValue().get(username);
    }
}
