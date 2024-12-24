package cafeLogProject.cafeLog.auth.exception;

import cafeLogProject.cafeLog.exception.CafeAppException;
import cafeLogProject.cafeLog.exception.ErrorCode;

public class TokenInvalidException extends CafeAppException {
    public TokenInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenInvalidException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
