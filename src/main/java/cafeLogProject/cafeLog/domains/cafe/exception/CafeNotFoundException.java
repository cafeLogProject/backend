package cafeLogProject.cafeLog.domains.cafe.exception;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class CafeNotFoundException extends CafeAppException{
    public CafeNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CafeNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
