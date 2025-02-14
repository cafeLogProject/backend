package cafeLogProject.cafeLog.common.exception.follow;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class FollowUserNotFoundException extends CafeAppException {
    public FollowUserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FollowUserNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
