package cafeLogProject.cafeLog.auth.jwt;

import cafeLogProject.cafeLog.auth.exception.UserNotAuthenticatedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static cafeLogProject.cafeLog.exception.ErrorCode.USER_NOT_AUTH_ERROR;

@Slf4j
@RequiredArgsConstructor
public class JWTLogoutFilter extends OncePerRequestFilter {

    private final JWTTokenService tokenService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/logout")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                log.warn("Unauthorized logout attempt.");
                throw new UserNotAuthenticatedException(USER_NOT_AUTH_ERROR);
            }

            String username = authentication.getName();

            tokenService.logout(username);

            removeCookie(response, "access");
            removeCookie(response, "refresh");

            log.info("User logout : {}", username);
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }

    private void removeCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0); // 쿠키 삭제를 위해 만료 시간을 0으로 설정
        cookie.setPath("/"); // 쿠키 경로 설정
        response.addCookie(cookie);
    }
}
