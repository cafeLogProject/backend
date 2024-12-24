package cafeLogProject.cafeLog.auth.jwt;

import cafeLogProject.cafeLog.auth.exception.UserExtractException;
import cafeLogProject.cafeLog.entity.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cafeLogProject.cafeLog.auth.jwt.TokenExpiration.ACCESS_TOKEN_EXPIRATION;
import static cafeLogProject.cafeLog.auth.jwt.TokenExpiration.REFRESH_TOKEN_EXPIRATION;
import static cafeLogProject.cafeLog.exception.ErrorCode.USER_EXTRACT_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class JWTTokenService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final AccessRepository accessRepository;

    /**
     * 사용자의 accessToken 이 만료되었다면, accessToken, refreshToken 둘 다 재발급
     */
    public String reissue(String refreshToken) {

        JWTUserDTO userInfo = extractUserInfoFromToken(refreshToken);

        String newAccess = createNewAccess(userInfo.getUserId(), userInfo.getUsername(), userInfo.getUserRole());
        String newRefresh = createNewRefresh(userInfo.getUserId(), userInfo.getUsername(), userInfo.getUserRole());

        refreshRepository.deleteByUsername(refreshToken);

        reissueAccessToken(userInfo.getUsername(), newAccess);
        reissueRefreshToken(userInfo.getUsername(), newRefresh);
        log.info("{} : Reissued token", userInfo.getUsername());

        return newAccess;
    }

    public void logout(String username) {
        refreshRepository.deleteByUsername(username);
        accessRepository.deleteByUsername(username);
    }

    /**
     * 토큰에서 사용자 정보 추출
     */
    @Transactional(readOnly = true)
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
     * 새로운 access 토큰 생성
     */
    @Transactional(readOnly = true)
    public String createNewAccess(Long userId, String username, UserRole role) {
        return jwtUtil.createJWT(userId, username, role, "access", ACCESS_TOKEN_EXPIRATION);
    }

    /**
     * 새로운 refresh 토큰 생성
     */
    @Transactional(readOnly = true)
    public String createNewRefresh(Long userId, String username, UserRole role) {
        return jwtUtil.createJWT(userId, username, role, "refresh", REFRESH_TOKEN_EXPIRATION);
    }

    /**
     * 새로 생성한 access 토큰을 저장
     */
    public void reissueAccessToken(String username, String newAccess) {
        AccessToken newAccessToken = new AccessToken();
        newAccessToken.setUsername(username);
        newAccessToken.setAccess(newAccess);

        accessRepository.save(newAccessToken);
    }

    /**
     * 새로 생성한 refresh 토큰을 저장
     */
    public void reissueRefreshToken(String username, String newRefresh) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setRefresh(newRefresh);

        refreshRepository.save(refreshToken);
    }
}
