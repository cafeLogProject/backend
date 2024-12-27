package cafeLogProject.cafeLog.domains.image.exception;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class ImageSaveException extends CafeAppException {
    public ImageSaveException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ImageSaveException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
