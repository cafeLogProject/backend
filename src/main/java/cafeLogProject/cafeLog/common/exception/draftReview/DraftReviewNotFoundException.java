package cafeLogProject.cafeLog.common.exception.draftReview;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class DraftReviewNotFoundException extends CafeAppException {
    public DraftReviewNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DraftReviewNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
