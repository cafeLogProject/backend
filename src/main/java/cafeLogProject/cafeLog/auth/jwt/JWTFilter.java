package cafeLogProject.cafeLog.auth.jwt;

import cafeLogProject.cafeLog.auth.exception.TokenExpiredException;
import cafeLogProject.cafeLog.auth.exception.TokenInvalidException;
import cafeLogProject.cafeLog.auth.exception.TokenNullException;
import cafeLogProject.cafeLog.auth.oauth2.CustomOAuth2User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static cafeLogProject.cafeLog.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final JWTTokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 로그인 페이지 요청은 JWT 검증을 건너뛰도록 변경
        if (path.equals("/login")) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = extractAccessToken(request, "access");
        String refreshToken = extractAccessToken(request, "refresh");

        validateRefreshToken(refreshToken, response);

        checkNullOrEmpty(accessToken);

        JWTUserDTO userDTO = validateAndExtractUserInfo(accessToken, refreshToken, response);

        authenticateUser(userDTO);

        chain.doFilter(request, response);
    }

    /**
     * 쿠키에서 AccessToken 추출
     */
    private String extractAccessToken(HttpServletRequest request, String tokenType) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenType)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void validateRefreshToken(String refreshToken, HttpServletResponse response) throws IOException {
        if (refreshToken == null || jwtUtil.isExpired(refreshToken)) {
            log.warn("Refresh token is expired");
            response.sendRedirect("/login"); // 로그인 페이지로 리다이렉트
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
     * AccessToken 이 만료되었는지, 유효한지 확인
     * 만료되었다면 AccessToken, RefreshToken 재발급
     * 
     * 위에 검증과 재발급 후에 JWTUserDTO 반환
     */
    private JWTUserDTO validateAndExtractUserInfo(String accessToken, String refreshToken, HttpServletResponse response) {
        try {
            jwtUtil.isExpired(accessToken);
            String tokenType = jwtUtil.getTokenType(accessToken);

            if (!"access".equals(tokenType)) {
                log.warn("Invalid access token type");
                throw new TokenInvalidException(TOKEN_INVALID_ERROR);
            }

            return tokenService.extractUserInfoFromToken(accessToken);

        } catch (ExpiredJwtException e) {
            log.warn("Token expired, attempting to reissue");

            JWTUserDTO userInfo = tokenService.extractUserInfoFromToken(refreshToken);

            String newAccessToken = tokenService.reissue(refreshToken);
            String newRefreshToken = tokenService.createNewRefresh(userInfo.getUserId(), userInfo.getUsername(), userInfo.getUserRole());

            response.addCookie(createCookie("access", newAccessToken));
            response.addCookie(createCookie("refresh", newRefreshToken));

            return userInfo;
        } catch (Exception e) {
            log.error("Invalid token: {}", e.getMessage());
            throw new TokenInvalidException(TOKEN_INVALID_ERROR);
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

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);

        cookie.setMaxAge(60 * 60 * 24);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
