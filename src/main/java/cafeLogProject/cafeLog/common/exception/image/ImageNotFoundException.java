package cafeLogProject.cafeLog.common.exception.image;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class ImageNotFoundException extends CafeAppException {
    public ImageNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ImageNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
