package cafeLogProject.cafeLog.common.auth.exception;


import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class UserNotAuthenticatedException extends CafeAppException {
    public UserNotAuthenticatedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserNotAuthenticatedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
