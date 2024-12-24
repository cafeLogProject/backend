package cafeLogProject.cafeLog.exception;

public class UserNotFoundException extends CafeAppException{
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
