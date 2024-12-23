package cafeLogProject.cafeLog.exception;

public class UserExtractException extends CafeAppException{
    public UserExtractException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserExtractException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
