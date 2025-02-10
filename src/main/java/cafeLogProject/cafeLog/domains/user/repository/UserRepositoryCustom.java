package cafeLogProject.cafeLog.domains.user.repository;

import cafeLogProject.cafeLog.api.user.dto.UserSearchRes;

import java.util.List;

public interface UserRepositoryCustom {

    boolean existsNicknameExcludingSelf(String username, String newNickname);
    List<UserSearchRes> findByNicknameContainingIgnoreCase(String nickname);

}
