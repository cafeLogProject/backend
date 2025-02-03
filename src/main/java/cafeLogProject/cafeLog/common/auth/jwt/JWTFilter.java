package cafeLogProject.cafeLog.common.auth.jwt;

import cafeLogProject.cafeLog.common.auth.exception.TokenExpiredException;
import cafeLogProject.cafeLog.common.auth.exception.TokenNotFoundException;
import cafeLogProject.cafeLog.common.auth.exception.TokenNullException;
import cafeLogProject.cafeLog.common.auth.jwt.token.JWTTokenService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import cafeLogProject.cafeLog.common.exception.UnexpectedServerException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.createCookie;
import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.extractToken;
import static cafeLogProject.cafeLog.common.config.SecurityConfig.whiteList;
import static cafeLogProject.cafeLog.common.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final JWTTokenService tokenService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // 화이트리스트에 있는 경로는 필터를 건너뛰기
        if (Arrays.asList(whiteList).contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = extractToken(request, "access");
        String refreshToken = extractToken(request, "refresh");

        validateRefreshToken(refreshToken);

        checkNullOrEmpty(accessToken);

        JWTUserDTO userDTO = extractUserInfo(accessToken, refreshToken, response);

        authenticateUser(userDTO);

        chain.doFilter(request, response);
    }


    /**
     * RefreshToken 이 유효한지 확인
     */
    private void validateRefreshToken(String refreshToken) {

        if (refreshToken == null || jwtUtil.isExpired(refreshToken)) {
            log.warn("Refresh token is expired");
            throw new TokenExpiredException(TOKEN_EXPIRED_ERROR); // 예외를 던져서 흐름을 중단
        }
    }

    /**
     *  AccessToken 이 null 이거나 값이 비어있는지 확인
     */
    private void checkNullOrEmpty(String accessToken) {

        if (accessToken == null || accessToken.isEmpty()) {
            log.debug("token null");
            throw new TokenNullException(TOKEN_NULL_ERROR);
        }
    }

    /**
     *  JWTUserDTO 반환  --> access 토큰이 만료되었다면 재발급 후 반환
     */
    private JWTUserDTO extractUserInfo(String accessToken, String refreshToken, HttpServletResponse response) {

        try {
            validateAccessToken(accessToken);

            return tokenService.extractUserInfoFromToken(accessToken);
        } catch (TokenExpiredException e) {

            return reissueToken(refreshToken, response);
        } catch (Exception e) {

            log.error("Unexpected error: {}", e.getMessage());
            throw new UnexpectedServerException(UNEXPECTED_ERROR);
        }
    }

    /**
     * 토큰 재발급 후에 JWTUserDTO 반환
     */
    private JWTUserDTO reissueToken(String refreshToken, HttpServletResponse response) {

        log.debug("Token expired, attempting to reissue");

        String reissuedUsername = tokenService.reissue(refreshToken);
        String newAccessToken = tokenService.getAccessTokenByUsername(reissuedUsername);
        String newRefreshToken = tokenService.getRefreshTokenByUsername(reissuedUsername);

        log.debug("New RefreshToken : {}", newRefreshToken);

        if (newAccessToken == null || newRefreshToken == null) {
            log.error("Failed to find new token, user: {}", reissuedUsername);
            throw new TokenNotFoundException(TOKEN_NOT_FOUND_ERROR);
        }

//        response.addCookie(createCookie("access", newAccessToken));
//        response.addCookie(createCookie("refresh", newRefreshToken));

        response.setHeader("Set-Cookie", "access=" + newAccessToken + "; HttpOnly; Path=/; SameSite=None");
        response.setHeader("Set-Cookie", "refresh=" + newRefreshToken + "; HttpOnly; Path=/; SameSite=None");

        return tokenService.extractUserInfoFromToken(newAccessToken);
    }

    /**
     * access 토큰 검사
     */
    private void validateAccessToken(String accessToken) {

        if (accessToken == null || jwtUtil.isExpired(accessToken)) {
            log.debug("Access token is expired, try reissue ...");
            throw new TokenExpiredException(TOKEN_EXPIRED_ERROR);
        }
    }

    /**
     * 생성한 UserDTO -> CustomOAuth2User -> 권한 인증 토큰 발급 -> 권한 인증
     */
    private static void authenticateUser(JWTUserDTO userDTO) {

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
