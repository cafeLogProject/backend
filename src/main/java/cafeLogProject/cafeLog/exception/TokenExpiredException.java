package cafeLogProject.cafeLog.exception;

public class TokenExpiredException extends CafeAppException{
    public TokenExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenExpiredException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
