package cafeLogProject.cafeLog.api.user.service;

import cafeLogProject.cafeLog.api.user.dto.UserSearchRes;
import cafeLogProject.cafeLog.api.user.dto.IsExistNicknameRes;
import cafeLogProject.cafeLog.api.user.dto.UserInfoRes;
import cafeLogProject.cafeLog.api.user.dto.UserUpdateReq;
import cafeLogProject.cafeLog.common.auth.jwt.JWTUserDTO;
import cafeLogProject.cafeLog.common.exception.user.UserNicknameException;
import cafeLogProject.cafeLog.common.exception.user.UserNicknameNullException;
import cafeLogProject.cafeLog.common.exception.user.UserNotFoundException;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cafeLogProject.cafeLog.common.exception.ErrorCode.*;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoRes getUserInfo(String userName) {

        User user = userRepository.findByUsername(userName).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR));

        return UserInfoRes.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .email(user.getEmail())
                .isProfileImageExist(user.isImageExist())
                .build();
    }

    @Transactional
    public void updateUser(String username, UserUpdateReq userUpdateReq) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR));

        validateNickname(username, userUpdateReq);

        user.updateUserNickname(userUpdateReq.getNickName());
        user.updateUserIntroduce(userUpdateReq.getIntroduce());

        userRepository.save(user);
    }

    public IsExistNicknameRes isExistNickname(String username, String nickname) {

        if (!userRepository.existsNicknameExcludingSelf(username, nickname)) {
            return new IsExistNicknameRes(nickname, false);
        }

        return new IsExistNicknameRes(nickname, true);
    }

    public List<UserSearchRes> searchUsersByNickname(String nickname) {

        if (nickname == null || nickname.trim().isEmpty()) {
            throw new UserNicknameNullException(USER_NICKNAME_NULL_ERROR);
        }
        return userRepository.findByNicknameContainingIgnoreCase(nickname);
    }

    private void validateNickname(String userName, UserUpdateReq userUpdateReq) {

        if (userUpdateReq.getNickName() != null && userRepository.existsNicknameExcludingSelf(userName, userUpdateReq.getNickName())) {
            log.warn("nickname is duplicate. user = {}, nickname = {}", userName, userUpdateReq.getNickName());
            throw new UserNicknameException(USER_NICKNAME_ERROR);
        }
    }

    public JWTUserDTO findByUsername(String username) {

        return userRepository.findByUsername(username)
                .map(user -> new JWTUserDTO(user.getId(), user.getUsername(), user.getRole()))
                .orElseThrow(() -> {
                    log.error("User not found: {}", username); // 로깅 추가
                    return new UserNotFoundException(USER_NOT_FOUND_ERROR);
                });
    }
}
