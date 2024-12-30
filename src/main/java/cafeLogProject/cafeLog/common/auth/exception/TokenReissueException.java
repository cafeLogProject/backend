package cafeLogProject.cafeLog.common.auth.exception;


import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class TokenReissueException extends CafeAppException {
    public TokenReissueException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenReissueException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
