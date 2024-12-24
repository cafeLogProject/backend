package cafeLogProject.cafeLog.auth.exception;

import cafeLogProject.cafeLog.exception.CafeAppException;
import cafeLogProject.cafeLog.exception.ErrorCode;

public class TokenNullException extends CafeAppException {
    public TokenNullException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenNullException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
