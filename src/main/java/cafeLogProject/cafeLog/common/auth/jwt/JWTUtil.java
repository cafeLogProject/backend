package cafeLogProject.cafeLog.common.auth.jwt;

import cafeLogProject.cafeLog.common.auth.exception.TokenInvalidException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.domains.user.domain.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/***
 * JWT 토큰을 생성, 검증하는 유틸 클래스
 */
@Slf4j
@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    /**
     *  JWT 토큰에서 (DB에 저장된)사용자 ID 추출
     */
    public Long getUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    /**
     * JWT 토큰에서 사용자 이름 추출
     */
    public String getUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    /**
     * JWT 토큰에서 사용자 역할 추출
     */
    public UserRole getRole(String token) {
        return UserRole.valueOf(parseClaims(token).get("role", String.class));
    }

    /**
     * JWT 토큰에서 access 추출
     */
    public String getTokenType(String token) {
        return parseClaims(token).get("tokenType", String.class);
    }

    /**
     * JWT 토큰의 만료여부 체크
     */
    public Boolean isExpired(String token) {
        try {
            return parseClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            log.warn("Token is invalid or expired: {}", e.getMessage());
            return true;
        }
    }

    /**
     * JWT 토큰 생성
     */
    public String createJWT(Long userId, String username, UserRole role, String tokenType, Long expiredMs) {

        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .claim("role", role.name())
                .claim("tokenType", tokenType)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    /**
     * JWT 의 페이로드에서 특정 정보를 추출하는 공통 메소드
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            log.error("Token parsing error: {}", e.getMessage());
            throw new TokenInvalidException(ErrorCode.TOKEN_INVALID_ERROR);
        }
    }
}
