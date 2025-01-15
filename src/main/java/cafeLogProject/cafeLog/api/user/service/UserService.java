package cafeLogProject.cafeLog.api.user.service;

import cafeLogProject.cafeLog.api.user.dto.UserUpdateReq;
import cafeLogProject.cafeLog.common.auth.jwt.JWTUserDTO;
import cafeLogProject.cafeLog.common.exception.user.UserNicknameException;
import cafeLogProject.cafeLog.common.exception.user.UserNotFoundException;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cafeLogProject.cafeLog.common.exception.ErrorCode.USER_NICKNAME_ERROR;
import static cafeLogProject.cafeLog.common.exception.ErrorCode.USER_NOT_FOUND_ERROR;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateUser(String userName, UserUpdateReq userUpdateReq) {

        User user = userRepository.findByUsername(userName).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR));

        validateNickname(userName, userUpdateReq);

        user.updateUserNickname(userUpdateReq.getNickName());
        user.updateUserIntroduce(userUpdateReq.getIntroduce());

        userRepository.save(user);
    }

    private void validateNickname(String userName, UserUpdateReq userUpdateReq) {
        if (userUpdateReq.getNickName() != null && userRepository.existsByNickname(userUpdateReq.getNickName())) {
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
