package cafeLogProject.cafeLog.auth.exception;

import cafeLogProject.cafeLog.exception.CafeAppException;
import cafeLogProject.cafeLog.exception.ErrorCode;

public class TokenExpiredException extends CafeAppException {
    public TokenExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenExpiredException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
