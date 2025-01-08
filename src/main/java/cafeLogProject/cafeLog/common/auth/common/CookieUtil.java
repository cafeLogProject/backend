package cafeLogProject.cafeLog.common.auth.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    /***
     * 쿠키 생성 메소드
     */
    public static Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);

        cookie.setMaxAge(60 * 60 * 24);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    /***
     * 쿠키 삭제 메소드
     */
    public static void removeCookie(HttpServletResponse response, String cookieName) {

        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0); // 쿠키 삭제를 위해 만료 시간을 0으로 설정
        cookie.setPath("/"); // 쿠키 경로 설정
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
