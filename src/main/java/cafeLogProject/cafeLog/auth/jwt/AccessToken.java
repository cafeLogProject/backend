package cafeLogProject.cafeLog.auth.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "access_token", timeToLive = TokenExpiration.REFRESH_TOKEN_EXPIRATION)
@Getter @Setter
public class AccessToken {

    @Id
    private String access;
    @Indexed
    private String username;
}
