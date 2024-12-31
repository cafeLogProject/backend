package cafeLogProject.cafeLog.common.exception.cafe;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class CafeSaveException extends CafeAppException {
    public CafeSaveException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CafeSaveException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
