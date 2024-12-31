package cafeLogProject.cafeLog.domains.review.exception;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class ReviewUpdateException extends CafeAppException {
    public ReviewUpdateException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReviewUpdateException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
