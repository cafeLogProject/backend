package cafeLogProject.cafeLog.api.user.service;

import cafeLogProject.cafeLog.common.auth.jwt.JWTUserDTO;
import cafeLogProject.cafeLog.common.exception.user.UserNotFoundException;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cafeLogProject.cafeLog.common.exception.ErrorCode.USER_NOT_FOUND_ERROR;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public JWTUserDTO findByUsername(String username) {

        return userRepository.findByUsername(username)
                .map(user -> new JWTUserDTO(user.getId(), user.getUsername(), user.getRole()))
                .orElseThrow(() -> {
                    log.error("User not found: {}", username); // 로깅 추가
                    return new UserNotFoundException(USER_NOT_FOUND_ERROR);
                });
    }
}
