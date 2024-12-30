package cafeLogProject.cafeLog.common.exception;


public class UnexpectedServerException extends CafeAppException{
    public UnexpectedServerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnexpectedServerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
