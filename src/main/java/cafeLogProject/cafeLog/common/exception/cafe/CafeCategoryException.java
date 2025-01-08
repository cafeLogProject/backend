package cafeLogProject.cafeLog.common.exception.cafe;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class CafeCategoryException extends CafeAppException {
    public CafeCategoryException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CafeCategoryException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
