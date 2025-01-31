package cafeLogProject.cafeLog.common.auth.exception;


import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class TokenNullException extends CafeAppException {

    public TokenNullException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenNullException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
