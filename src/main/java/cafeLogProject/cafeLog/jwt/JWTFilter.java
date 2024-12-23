package cafeLogProject.cafeLog.jwt;

import cafeLogProject.cafeLog.entity.enums.UserRole;
import cafeLogProject.cafeLog.exception.*;
import cafeLogProject.cafeLog.oauth2.CustomOAuth2User;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String accessToken = extractAccessToken(request);

        checkNullOrEmpty(accessToken);

        isValidToken(accessToken, response);

        JWTUserDTO userDTO = extractUserInfo(accessToken);

        authenticateUser(userDTO);

        chain.doFilter(request, response);
    }

    /**
     * 쿠키에서 AccessToken 추출
     */
    private String extractAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")){
                    log.info("access={}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
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
     */
    private void isValidToken(String accessToken, HttpServletResponse response) {
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            log.warn("token expired");
            throw new TokenExpiredException(TOKEN_EXPIRED_ERROR);
        } catch (Exception e) {
            log.error("invalid token : {}", e.getMessage());
            throw new TokenInvalidException(TOKEN_INVALID_ERROR);
        }

        if (!jwtUtil.getAccessToken(accessToken).equals("access")) {
            log.warn("invalid access token");
            throw new TokenInvalidException(TOKEN_INVALID_ERROR);
        }
    }

    /**
     *  AccessToken 에서 유저 정보 추출 -> JWTUserDTO 생성
     */
    private JWTUserDTO extractUserInfo(String accessToken) {
        Long userId;
        String username;
        UserRole role;
        try {
            userId = jwtUtil.getUserId(accessToken);
            username = jwtUtil.getUsername(accessToken);
            role = jwtUtil.getRole(accessToken);
        } catch (Exception e) {
            log.error("Failed to extract user information from token: {}", e.getMessage());
            throw new UserExtractException(USER_EXTRACT_ERROR);
        }

        return new JWTUserDTO(userId, username, role);
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
