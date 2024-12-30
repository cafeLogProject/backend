package cafeLogProject.cafeLog.common.dto;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiErrorResponse {
    private HttpStatus status;
    private String message;

    public ApiErrorResponse(CafeAppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        this.status = errorCode.getHttpStatus();
        this.message = errorCode.getMessage();
    }

    public ApiErrorResponse(HttpStatus status, String errorMsg) {
        this.status = status;
        this.message = errorMsg;
    }
}
