package cafeLogProject.cafeLog.common.exception.user;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class UserNicknameNullException extends CafeAppException {
    public UserNicknameNullException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserNicknameNullException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
