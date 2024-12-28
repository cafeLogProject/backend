package cafeLogProject.cafeLog.auth.jwt;

public class TokenExpiration {

    public static final long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000L; // 60분
    public static final long REFRESH_TOKEN_EXPIRATION = 14 * 24 * 60 * 60 * 1000L; // 14일
}
