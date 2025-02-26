package cafeLogProject.cafeLog.common.auth.jwt;

import cafeLogProject.cafeLog.common.auth.jwt.token.JWTTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.extractToken;
import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.removeCookie;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTLogoutHandler implements LogoutHandler {

    private final JWTTokenService jwtTokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String refresh = extractToken(request, "refresh");

        if (refresh == null || refresh.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        removeCookie(response, "access");
        removeCookie(response, "refresh");

        String username = jwtTokenService.extractUserInfoFromToken(refresh).getUsername();
        jwtTokenService.addBlacklist(username, refresh);

        log.info("User Logout = {}", username);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
