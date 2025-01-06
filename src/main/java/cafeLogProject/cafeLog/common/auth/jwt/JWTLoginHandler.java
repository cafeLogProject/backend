package cafeLogProject.cafeLog.common.auth.jwt;

import cafeLogProject.cafeLog.common.auth.jwt.token.JWTTokenService;
import cafeLogProject.cafeLog.api.user.service.UserService;
import cafeLogProject.cafeLog.domains.user.domain.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.createCookie;
import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.createResponseCookie;

@Component
@RequiredArgsConstructor
public class JWTLoginHandler implements AuthenticationSuccessHandler {

    @Value("${frontend.redirectUrl}")
    private String frontendRedirectUrl;

    private final JWTTokenService tokenService;
    private final UserService userService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        JWTUserDTO userDTO = userService.findByUsername(username);
        Long userId = userDTO.getUserId();
        UserRole role = userDTO.getUserRole();

        tokenService.deleteTokenByUsername(username);
        String access = tokenService.createNewAccess(userId, username, role);
        String refresh = tokenService.createNewRefresh(userId, username, role);
        tokenService.reissueAccessToken(username, access);
        tokenService.reissueRefreshToken(username, refresh);

        // ResponseCookie로 변환
        ResponseCookie accessTokenCookie = createResponseCookie("access", access);
        ResponseCookie refreshTokenCookie = createResponseCookie("refresh", refresh);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        response.sendRedirect(frontendRedirectUrl + "/oauth/redirect");
    }
}
