package cafeLogProject.cafeLog.common.exception.review;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class TagNotFoundException extends CafeAppException {
    public TagNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TagNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
