package cafeLogProject.cafeLog.auth.jwt.token;

import cafeLogProject.cafeLog.auth.jwt.TokenExpiration;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "access", timeToLive = TokenExpiration.REFRESH_TOKEN_EXPIRATION)
@Getter @NoArgsConstructor
public class AccessToken {

    @Id
    private String access;
    @Indexed
    private String username;

    @Builder
    public AccessToken(String access, String username) {
        this.access = access;
        this.username = username;
    }
}
