package cafeLogProject.cafeLog.auth.exception;

import cafeLogProject.cafeLog.exception.CafeAppException;
import cafeLogProject.cafeLog.exception.ErrorCode;

public class UserExtractException extends CafeAppException {
    public UserExtractException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserExtractException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
