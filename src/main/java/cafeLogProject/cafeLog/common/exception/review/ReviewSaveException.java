package cafeLogProject.cafeLog.common.exception.review;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class ReviewSaveException extends CafeAppException {
    public ReviewSaveException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReviewSaveException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
