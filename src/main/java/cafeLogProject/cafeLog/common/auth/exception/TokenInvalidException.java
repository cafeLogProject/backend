package cafeLogProject.cafeLog.common.auth.exception;


import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class TokenInvalidException extends CafeAppException {
    public TokenInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenInvalidException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
