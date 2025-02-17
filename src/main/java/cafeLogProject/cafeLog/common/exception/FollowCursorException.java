package cafeLogProject.cafeLog.common.exception;

public class FollowCursorException extends CafeAppException{
    public FollowCursorException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FollowCursorException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
