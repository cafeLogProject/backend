package cafeLogProject.cafeLog.service;

import cafeLogProject.cafeLog.auth.jwt.JWTUserDTO;
import cafeLogProject.cafeLog.exception.ErrorCode;
import cafeLogProject.cafeLog.exception.UserNotFoundException;
import cafeLogProject.cafeLog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    return new UserNotFoundException(ErrorCode.USER_NOT_FOUND_ERROR);
                });
    }
}
