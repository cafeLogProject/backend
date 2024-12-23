package cafeLogProject.cafeLog.exception;

public class UserCreateException extends CafeAppException{

    public UserCreateException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserCreateException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
