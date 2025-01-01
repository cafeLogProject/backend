package cafeLogProject.cafeLog.common.auth.jwt.token;

import cafeLogProject.cafeLog.common.auth.ExpiredCheckDTO;
import cafeLogProject.cafeLog.common.auth.exception.UserExtractException;
import cafeLogProject.cafeLog.common.auth.jwt.JWTUserDTO;
import cafeLogProject.cafeLog.common.auth.jwt.JWTUtil;
import cafeLogProject.cafeLog.domains.user.domain.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cafeLogProject.cafeLog.common.auth.jwt.TokenExpiration.*;
import static cafeLogProject.cafeLog.common.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JWTTokenService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final AccessRepository accessRepository;

    /**
     * 사용자의 accessToken 이 만료되었다면, accessToken, refreshToken 둘 다 재발급
     */
    @Transactional
    public String reissue(String refreshToken) {

        JWTUserDTO userInfo = extractUserInfoFromToken(refreshToken);

        String newAccess = createNewAccess(userInfo.getUserId(), userInfo.getUsername(), userInfo.getUserRole());
        String newRefresh = createNewRefresh(userInfo.getUserId(), userInfo.getUsername(), userInfo.getUserRole());

        refreshRepository.deleteByUsername(userInfo.getUsername());

        reissueAccessToken(userInfo.getUsername(), newAccess);
        reissueRefreshToken(userInfo.getUsername(), newRefresh);
        log.info("{} : Reissued token", userInfo.getUsername());

        return userInfo.getUsername();
    }

    @Transactional
    public void deleteTokenByUsername(String username) {

        refreshRepository.deleteByUsername(username);
        accessRepository.deleteByUsername(username);
    }

    /**
     * 토큰에서 사용자 정보 추출
     */
    public JWTUserDTO extractUserInfoFromToken(String Token) {

        try {
            Long userId = jwtUtil.getUserId(Token);
            String username = jwtUtil.getUsername(Token);
            UserRole role = jwtUtil.getRole(Token);

            return new JWTUserDTO(userId, username, role);
        } catch (Exception e) {
            log.error("Failed to extract user info from token: {}", e.getMessage());
            throw new UserExtractException(USER_EXTRACT_ERROR);
        }
    }

    /**
     * 사용자 이름으로 access 토큰 반환
     */
    public String getAccessTokenByUsername(String username) {

        return accessRepository.findByUsername(username).getAccess();
    }

    /**
     * 사용자 이름으로 refresh 토큰 반환
     */
    public String getRefreshTokenByUsername(String username) {

        return refreshRepository.findByUsername(username).getRefresh();
    }


    /**
     * 새로운 access 토큰 생성
     */
    public String createNewAccess(Long userId, String username, UserRole role) {

        return jwtUtil.createJWT(userId, username, role, "access", ACCESS_TOKEN_EXPIRATION);
    }

    /**
     * 새로운 refresh 토큰 생성
     */
    public String createNewRefresh(Long userId, String username, UserRole role) {

        return jwtUtil.createJWT(userId, username, role, "refresh", REFRESH_TOKEN_EXPIRATION);
    }

    /**
     * 새로 생성한 access 토큰을 저장
     */
    public void reissueAccessToken(String username, String newAccess) {

        accessRepository.save(
                AccessToken
                .builder()
                .username(username)
                .access(newAccess)
                .build());
    }

    /**
     * 새로 생성한 refresh 토큰을 저장
     */
    public void reissueRefreshToken(String username, String newRefresh) {

        refreshRepository.save(
                RefreshToken
                        .builder()
                        .username(username)
                        .refresh(newRefresh)
                        .build()
        );
    }

    public ExpiredCheckDTO checkTokenIsExpired(String accessToken, String refreshToken) {

        return new ExpiredCheckDTO(jwtUtil.isExpired(accessToken), jwtUtil.isExpired(refreshToken));
    }
}
