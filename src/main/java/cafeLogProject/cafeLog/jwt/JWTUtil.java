package cafeLogProject.cafeLog.jwt;

import cafeLogProject.cafeLog.entity.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/***
 * JWT 토큰을 생성, 검증하는 유틸 클래스
 */
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
        return parseClaims(token).get("role", UserRole.class);
    }

    /**
     * JWT 토큰에서 access 추출
     */
    public String getAccessToken(String token) {
        return parseClaims(token).get("access", String.class);
    }

    /**
     * JWT 토큰의 만료여부 체크
     */
    public Boolean isExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    /**
     * JWT 토큰 생성
     */
    public String createJWT(Long userId, String username, UserRole role, String access, Long expiredMs) {

        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .claim("role", role)
                .claim("access", access)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    /**
     * JWT 의 페이로드에서 특정 정보를 추출하는 공통 메소드
     */
    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
