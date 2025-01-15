package cafeLogProject.cafeLog.common.exception.user;

import cafeLogProject.cafeLog.common.exception.CafeAppException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;

public class UserNicknameException extends CafeAppException {
    public UserNicknameException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserNicknameException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
