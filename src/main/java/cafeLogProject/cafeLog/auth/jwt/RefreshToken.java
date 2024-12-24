package cafeLogProject.cafeLog.auth.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refresh_token", timeToLive = TokenExpiration.REFRESH_TOKEN_EXPIRATION)
@Getter @Setter
public class RefreshToken {

    @Id
    private String refresh;
    @Indexed
    private String username;
}
