package cafeLogProject.cafeLog.common.exception.cafe;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class CafeApiParsingException extends CafeAppException {
    public CafeApiParsingException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CafeApiParsingException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
