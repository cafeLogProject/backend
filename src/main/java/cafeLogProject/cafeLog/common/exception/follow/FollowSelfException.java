package cafeLogProject.cafeLog.common.exception.follow;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class FollowSelfException extends CafeAppException {
    public FollowSelfException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FollowSelfException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
