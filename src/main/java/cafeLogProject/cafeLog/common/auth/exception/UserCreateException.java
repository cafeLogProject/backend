package cafeLogProject.cafeLog.common.auth.exception;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class UserCreateException extends CafeAppException {

    public UserCreateException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserCreateException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
