package cafeLogProject.cafeLog.common.auth.jwt.token;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccessRepository {

    private final RedisTemplate<String, AccessToken> redisTemplate;

    public AccessRepository(RedisTemplate<String, AccessToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void deleteByUsername(String username) {
        redisTemplate.delete("access:" + username);
    }

    public void save(AccessToken accessToken) {
        redisTemplate.opsForValue().set("access:" + accessToken.getUsername(), accessToken);
    }

    public AccessToken findByUsername(String username) {
        return redisTemplate.opsForValue().get("access:" + username);
    }
}
