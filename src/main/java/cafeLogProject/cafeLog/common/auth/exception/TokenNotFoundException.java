package cafeLogProject.cafeLog.common.auth.exception;


import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class TokenNotFoundException extends CafeAppException {
    public TokenNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
