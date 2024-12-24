package cafeLogProject.cafeLog.auth.jwt;

import cafeLogProject.cafeLog.entity.enums.UserRole;
import cafeLogProject.cafeLog.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

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


        String access = tokenService.createNewAccess(userId, username, role);
        String refresh = tokenService.createNewRefresh(userId, username, role);

        tokenService.reissueAccessToken(username, access);
        tokenService.reissueRefreshToken(username, refresh);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        response.addCookie(createCookie("access", access));
        response.addCookie(createCookie("refresh", refresh));
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
