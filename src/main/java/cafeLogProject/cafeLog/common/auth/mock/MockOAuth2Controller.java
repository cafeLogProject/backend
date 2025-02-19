package cafeLogProject.cafeLog.common.auth.mock;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/mock/oauth2")
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class MockOAuth2Controller {

    private static final Map<String, MockUserInfo> USER_STORE = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource resource = new ClassPathResource("mock/data/users/mock-users.json");
            List<MockUserInfo> users = mapper.readValue(
                resource.getInputStream(),
                mapper.getTypeFactory().constructCollectionType(List.class, MockUserInfo.class)
            );
            
            users.forEach(user -> USER_STORE.put(user.email(), user));
            
        } catch (IOException e) {
            log.error("Failed to load mock users", e);
            // Fallback to default user if loading fails
            USER_STORE.put("default@example.com", 
                new MockUserInfo("000000", "default@example.com", "Default User"));
        }
    }

    /**
     * 1) Authorization Code 발급
     *  - Spring Security가 /oauth2/authorization/google로 접근 시
     *  - 여기로 리다이렉트 하게 됨 (application-dev.yml 설정 덕분)
     *  - mock_email 파라미터로 어떤 유저로 로그인할지 지정 가능
     */
    @GetMapping("/authorize")
    public void authorize(
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("state") String state,
            @RequestParam(value="mock_email", required=false) String mockEmail,
            HttpServletResponse response
    ) throws IOException {
        if (mockEmail == null) {
            // 파라미터 미지정 시 기본 유저
            mockEmail = "default@example.com";
        }

        // code에 mockEmail 정보를 담아서 토큰 발급 시점에 식별 가능하도록
        String code = Base64.getEncoder().encodeToString(mockEmail.getBytes());
        // OAuth 표준대로 redirect_uri + "?code=..." + "&state=..."
        response.sendRedirect(redirectUri + "?code=" + code + "&state=" + state);
    }

    /**
     * 2) 토큰 교환
     *  - Spring Security가 code를 POST로 보내옴
     *  - 여기서 code를 풀어서(디코딩) 어떤 유저인지 식별
     *  - 가짜 access_token을 만들어 JSON 반환
     */
    @PostMapping("/token")
    public Map<String, String> token(@RequestParam String code) {
        String mockEmail = new String(Base64.getDecoder().decode(code));

        // 가짜 토큰 생성
        String accessToken = "mock-access-token-for-" + mockEmail;

        return Map.of(
                "access_token", accessToken,
                "token_type", "Bearer",
                "expires_in", "3600"
        );
    }

    /**
     * 3) 유저정보 조회
     *  - Spring Security가 /userinfo 엔드포인트에 Authorization: Bearer ...
     *  - 여기서 access_token을 파싱해 mockEmail을 다시 꺼냄
     *  - 해당 유저의 정보(구글 userinfo 형태 비슷하게) JSON으로 반환
     */
    @GetMapping("/userinfo")
    public Map<String, Object> userinfo(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String emailPart = token.substring(token.indexOf("for-") + 4);

        MockUserInfo mockUser = USER_STORE.get(emailPart);
        if (mockUser == null) {
            return Map.of("error", "invalid_user");
        }

        // 실제 구글 userinfo 응답 형태를 흉내내기: "sub", "email", "name", 등
        return Map.of(
                "sub", mockUser.id(),
                "email", mockUser.email(),
                "name", mockUser.displayName()
        );
    }

    record MockUserInfo(String id, String email, String displayName) {}
}
