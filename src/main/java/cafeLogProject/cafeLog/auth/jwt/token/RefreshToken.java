package cafeLogProject.cafeLog.auth.jwt.token;

import cafeLogProject.cafeLog.auth.jwt.TokenExpiration;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refresh", timeToLive = TokenExpiration.REFRESH_TOKEN_EXPIRATION)
@Getter @NoArgsConstructor
public class RefreshToken {

    @Id
    private String refresh;
    @Indexed
    private String username;

    @Builder
    public RefreshToken(String refresh, String username) {
        this.refresh = refresh;
        this.username = username;
    }
}
