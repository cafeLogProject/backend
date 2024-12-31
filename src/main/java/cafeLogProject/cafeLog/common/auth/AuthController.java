package cafeLogProject.cafeLog.common.auth;

import cafeLogProject.cafeLog.common.auth.jwt.JWTUtil;
import cafeLogProject.cafeLog.common.auth.jwt.token.JWTTokenService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.extractToken;
import static cafeLogProject.cafeLog.common.auth.common.CookieUtil.removeCookie;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JWTTokenService tokenService;
    private final JWTUtil jwtUtil;

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
    public ResponseEntity<ExpiredCheckDTO> expiredCheck(HttpServletRequest request) {
        String accessToken = extractToken(request, "access"); // 엑세스 토큰 추출
        if (accessToken == null || jwtUtil.isExpired(accessToken)) {
            // 엑세스 토큰이 없거나 만료된 경우
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body(new ExpiredCheckDTO(true, false)); // 만료 상태 반환
        }

        // 유효한 토큰인 경우, 사용자 정보를 가져오기
        String username = jwtUtil.getUsername(accessToken);
        ExpiredCheckDTO checkResult = tokenService.checkTokenIsExpired(username);
        return ResponseEntity.ok(tokenService.checkTokenIsExpired(username));
    }


}
