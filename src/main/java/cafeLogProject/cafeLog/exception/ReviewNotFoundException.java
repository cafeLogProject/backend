package cafeLogProject.cafeLog.exception;

public class ReviewNotFoundException extends CafeAppException{

    public ReviewNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReviewNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
