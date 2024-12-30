package cafeLogProject.cafeLog.common.exception;

import lombok.Getter;

@Getter
public class CafeAppException extends RuntimeException{
    private final ErrorCode errorCode;

    public CafeAppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CafeAppException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
