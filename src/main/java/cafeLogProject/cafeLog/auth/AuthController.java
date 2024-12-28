package cafeLogProject.cafeLog.auth;

import cafeLogProject.cafeLog.auth.jwt.token.JWTTokenService;
import cafeLogProject.cafeLog.auth.oauth2.CustomOAuth2User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static cafeLogProject.cafeLog.auth.common.CookieUtil.removeCookie;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JWTTokenService tokenService;

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> loginPage(HttpServletResponse response) {

        removeCookie(response, "access");
        removeCookie(response, "refresh");

        Map<String, String> loginLink = new HashMap<>();
        loginLink.put("google", "/oauth2/authorization/google");
        loginLink.put("naver", "/oauth2/authorization/naver");
        loginLink.put("facebook", "/oauth2/authorization/facebook");

        return ResponseEntity.ok(loginLink);
    }

    @GetMapping("/testing")
    public String testing() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("시큐리티 컨텍스트 홀더 테스트 : {}", username);
        return username;
    }

    @GetMapping("/check")
    public ResponseEntity<ExpiredCheckDTO> expiredCheck(@AuthenticationPrincipal CustomOAuth2User user) {

        return ResponseEntity.ok(tokenService.checkTokenIsExpired(user.getName()));
    }


}
