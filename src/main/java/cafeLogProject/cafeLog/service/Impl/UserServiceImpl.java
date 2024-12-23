package cafeLogProject.cafeLog.service.Impl;

import cafeLogProject.cafeLog.dto.RegistUserRequest;
import cafeLogProject.cafeLog.entity.Cafe;
import cafeLogProject.cafeLog.entity.User;
import cafeLogProject.cafeLog.repository.UserRepository;
import cafeLogProject.cafeLog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(RegistUserRequest registUserRequest) {
        // 계정 중복 여부 확인은 oauth2로 처리됨을 전제로 함
        User user = registUserRequest.toEntity();
        return userRepository.save(user);
    }

    @Override
    public User findUserById(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) return null;
        return userOptional.get();
    }

}
