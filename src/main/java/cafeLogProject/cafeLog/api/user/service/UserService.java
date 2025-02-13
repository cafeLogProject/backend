package cafeLogProject.cafeLog.api.user.service;

import cafeLogProject.cafeLog.api.user.dto.*;
import cafeLogProject.cafeLog.common.auth.jwt.JWTUserDTO;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import cafeLogProject.cafeLog.common.exception.user.UserNicknameException;
import cafeLogProject.cafeLog.common.exception.user.UserNicknameNullException;
import cafeLogProject.cafeLog.common.exception.user.UserNotFoundException;
import cafeLogProject.cafeLog.domains.review.repository.ReviewRepository;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static cafeLogProject.cafeLog.common.exception.ErrorCode.*;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public UserInfoRes getUserInfo(String username) {

        return userRepository.findMyProfileWithReviewCount(username)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR));
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

    public OtherUserInfoRes getOtherUserInfo(String currentUsername, Long otherUserId) {

        return userRepository.findOtherUserInfo(currentUsername, otherUserId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR));
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
