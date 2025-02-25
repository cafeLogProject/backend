package cafeLogProject.cafeLog.common.auth.jwt;

import cafeLogProject.cafeLog.common.auth.jwt.token.JWTTokenService;
import cafeLogProject.cafeLog.api.user.service.UserService;
import cafeLogProject.cafeLog.domains.user.domain.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.addResponseCookie;
import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.createCookie;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTLoginHandler implements AuthenticationSuccessHandler {

    @Value("${front.redirect}")
    private String frontendRedirect;
    private final JWTTokenService tokenService;
    private final UserService userService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        JWTUserDTO userDTO = userService.findByUsername(username);
        Long userId = userDTO.getUserId();
        UserRole role = userDTO.getUserRole();

        String access = tokenService.createNewAccess(userId, username, role);
        String refresh = tokenService.createNewRefresh(userId, username, role);

        if (tokenService.isExistInBlacklist(refresh)) {
            tokenService.deleteTokenInBlacklist(refresh);
        }

        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());

        ResponseCookie accessCookie = createCookie("access", access);
        ResponseCookie refreshCookie = createCookie("refresh", refresh);

        addResponseCookie(response, accessCookie);
        addResponseCookie(response, refreshCookie);

        response.getWriter().write("{\"message\":\"Login successful\"}");
        if (!frontendRedirect.equals("dev")) {
            response.sendRedirect(frontendRedirect);
        }

    }
}
