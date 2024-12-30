package cafeLogProject.cafeLog.common.auth.exception;


import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class UserExtractException extends CafeAppException {
    public UserExtractException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserExtractException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
