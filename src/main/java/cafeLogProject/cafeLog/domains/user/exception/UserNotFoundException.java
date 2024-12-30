package cafeLogProject.cafeLog.domains.user.exception;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class UserNotFoundException extends CafeAppException {
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
