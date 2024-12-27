package cafeLogProject.cafeLog.domains.user.service;

import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.dto.RegistUserRequest;
import cafeLogProject.cafeLog.domains.user.exception.UserNotFoundException;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(RegistUserRequest registUserRequest) {
        // 로그인 기능 깃헙 merge 후 해당함수 삭제
        User user = registUserRequest.toEntity();
        return userRepository.save(user);
    }

    @Override
    public User findUserById(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND_ERROR);

        return userOptional.get();
    }

}
