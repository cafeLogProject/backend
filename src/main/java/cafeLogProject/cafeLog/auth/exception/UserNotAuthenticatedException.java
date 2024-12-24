package cafeLogProject.cafeLog.auth.exception;

import cafeLogProject.cafeLog.exception.CafeAppException;
import cafeLogProject.cafeLog.exception.ErrorCode;

public class UserNotAuthenticatedException extends CafeAppException {
    public UserNotAuthenticatedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserNotAuthenticatedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
