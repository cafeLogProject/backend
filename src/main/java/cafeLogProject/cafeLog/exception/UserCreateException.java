package cafeLogProject.cafeLog.exception;

public class UserCreateException extends CafeAppException{

    public UserCreateException() {
        super(ErrorCode.USER_CREATE_ERROR);
    }

    public UserCreateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
