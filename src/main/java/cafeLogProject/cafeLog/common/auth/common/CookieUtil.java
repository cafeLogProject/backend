package cafeLogProject.cafeLog.common.auth.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

//    /***
//     * 쿠키 생성 메소드
//     */
//    public static Cookie createCookie(String key, String value) {
//
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(60 * 60 * 24);
////        cookie.setSecure(true);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        return cookie;
//    }
//
//    /***
//     * 쿠키 삭제 메소드
//     */
//    public static void removeCookie(HttpServletResponse response, String cookieName) {
//
//        Cookie cookie = new Cookie(cookieName, null);
//        cookie.setMaxAge(0); // 쿠키 삭제를 위해 만료 시간을 0으로 설정
//        cookie.setPath("/"); // 쿠키 경로 설정
//        response.addCookie(cookie);
//    }

    /***
     * 쿠키 생성 메소드
     */
    public static ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(60 * 60 * 24)
                .sameSite("None") // SameSite 속성 설정
                .build();
    }

    /***
     * 쿠키 삭제 메소드
     */
    public static void removeCookie(HttpServletResponse response, String cookieName) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, null)
                .maxAge(0) // 쿠키 삭제를 위해 만료 시간을 0으로 설정
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /***
     * 쿠키를 HttpServletResponse에 추가하는 메소드
     */
    public static void addResponseCookie(HttpServletResponse response, ResponseCookie responseCookie) {

        Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
        cookie.setPath(responseCookie.getPath());
        cookie.setHttpOnly(responseCookie.isHttpOnly());
        cookie.setSecure(responseCookie.isSecure());
        cookie.setMaxAge(60 * 60 * 24);

        response.addCookie(cookie);
    }


    /***
     * 쿠키에서 AccessToken or RefreshToken 추출
     */
    public static String extractToken(HttpServletRequest request, String tokenType) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenType)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
