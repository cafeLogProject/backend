package cafeLogProject.cafeLog.exception;

public class TokenInvalidException extends CafeAppException{
    public TokenInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenInvalidException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
