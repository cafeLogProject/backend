package cafeLogProject.cafeLog.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import static cafeLogProject.cafeLog.jwt.TokenExpiration.REFRESH_TOKEN_EXPIRATION;

@RedisHash(value = "refresh_token", timeToLive = REFRESH_TOKEN_EXPIRATION)
@Getter @Setter
public class RefreshToken {

    @Id
    @Indexed
    private String refresh;
    private String username;
}
