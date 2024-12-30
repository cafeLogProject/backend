package cafeLogProject.cafeLog.common.auth.exception;


import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class TokenExpiredException extends CafeAppException {
    public TokenExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenExpiredException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
