package cafeLogProject.cafeLog.auth.exception;

import cafeLogProject.cafeLog.exception.CafeAppException;
import cafeLogProject.cafeLog.exception.ErrorCode;

public class UserCreateException extends CafeAppException {

    public UserCreateException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserCreateException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
