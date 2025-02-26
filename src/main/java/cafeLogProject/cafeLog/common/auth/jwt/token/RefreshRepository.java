package cafeLogProject.cafeLog.common.auth.jwt.token;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshRepository {

    private final RedisTemplate<String, RefreshToken> redisTemplate;

    public RefreshRepository(RedisTemplate<String, RefreshToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void delete(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }

    public void save(RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(refreshToken.getRefresh(), refreshToken);
    }

    public boolean isExistInBlacklist(String refreshToken) {

        return redisTemplate.hasKey(refreshToken);
    }
}
