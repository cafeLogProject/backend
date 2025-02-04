package cafeLogProject.cafeLog.common.exception.image;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class ImageDamagedException extends CafeAppException {
    public ImageDamagedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ImageDamagedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
