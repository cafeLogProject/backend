package cafeLogProject.cafeLog.domains.image.exception;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class ImageDeleteException extends CafeAppException {
    public ImageDeleteException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ImageDeleteException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
