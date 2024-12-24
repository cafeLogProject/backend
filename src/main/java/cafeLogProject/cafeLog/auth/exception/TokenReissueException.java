package cafeLogProject.cafeLog.auth.exception;

import cafeLogProject.cafeLog.exception.CafeAppException;
import cafeLogProject.cafeLog.exception.ErrorCode;

public class TokenReissueException extends CafeAppException {
    public TokenReissueException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenReissueException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
