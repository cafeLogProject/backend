package cafeLogProject.cafeLog.common.exception.image;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class ImageInvalidException extends CafeAppException {
    public ImageInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ImageInvalidException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
