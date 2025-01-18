package cafeLogProject.cafeLog.common.exception.review;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class TagInvalidException extends CafeAppException {
    public TagInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TagInvalidException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
