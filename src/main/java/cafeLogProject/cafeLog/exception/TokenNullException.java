package cafeLogProject.cafeLog.exception;

public class TokenNullException extends CafeAppException{
    public TokenNullException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenNullException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
