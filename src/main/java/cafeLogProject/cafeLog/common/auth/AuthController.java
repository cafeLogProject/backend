package cafeLogProject.cafeLog.common.auth;

import cafeLogProject.cafeLog.common.auth.jwt.JWTUtil;
import cafeLogProject.cafeLog.common.auth.jwt.token.JWTTokenService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.*;
import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.addResponseCookie;

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

    @GetMapping("/success")
    public String testing(@AuthenticationPrincipal CustomOAuth2User user) {

        return "로그인 성공 : " + user.getName();
    }

    @GetMapping("/check")
    public ResponseEntity<ExpiredCheckDTO> expiredCheck(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = extractToken(request, "access"); // 엑세스 토큰 추출
        String refreshToken = extractToken(request, "refresh");

        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());

        ResponseCookie accessCookie = createCookie("access", accessToken);
        ResponseCookie refreshCookie = createCookie("refresh", refreshToken);

        addResponseCookie(response, accessCookie);
        addResponseCookie(response, refreshCookie);

        return ResponseEntity.ok(tokenService.checkTokenIsExpired(accessToken, refreshToken));

    }

}
