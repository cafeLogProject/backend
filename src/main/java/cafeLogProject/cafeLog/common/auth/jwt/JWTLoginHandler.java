package cafeLogProject.cafeLog.common.auth.jwt;

import cafeLogProject.cafeLog.common.auth.jwt.token.JWTTokenService;
import cafeLogProject.cafeLog.api.user.service.UserService;
import cafeLogProject.cafeLog.domains.user.domain.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.createCookie;

@Component
@RequiredArgsConstructor
public class JWTLoginHandler implements AuthenticationSuccessHandler {

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

        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        response.addCookie(createCookie("access", access));
        response.addCookie(createCookie("refresh", refresh));

        response.sendRedirect("/api/auth/success");
    }


}
