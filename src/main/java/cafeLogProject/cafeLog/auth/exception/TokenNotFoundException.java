package cafeLogProject.cafeLog.auth.exception;

import cafeLogProject.cafeLog.exception.CafeAppException;
import cafeLogProject.cafeLog.exception.ErrorCode;

public class TokenNotFoundException extends CafeAppException {
    public TokenNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
