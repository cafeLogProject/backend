package cafeLogProject.cafeLog.common.auth.jwt;

import cafeLogProject.cafeLog.common.auth.jwt.token.JWTTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.*;
import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.removeCookie;

@Slf4j
@RequiredArgsConstructor
public class JWTLogoutFilter extends OncePerRequestFilter {

    private final JWTTokenService tokenService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (!requestURI.matches("^\\/logout$")) {

            chain.doFilter(request, response);
            return;
        }

        String refresh = extractToken(request, "refresh");
        JWTUserDTO userDTO = tokenService.extractUserInfoFromToken(refresh);
        tokenService.deleteTokenByUsername(userDTO.getUsername());
        log.info("{} : 로그아웃", userDTO.getUsername());
        removeCookie(response, "access");
        removeCookie(response, "refresh");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
