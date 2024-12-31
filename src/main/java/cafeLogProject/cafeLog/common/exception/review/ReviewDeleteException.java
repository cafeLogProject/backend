package cafeLogProject.cafeLog.domains.review.exception;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class ReviewDeleteException extends CafeAppException {
    public ReviewDeleteException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReviewDeleteException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
