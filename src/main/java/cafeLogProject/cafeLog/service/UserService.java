package cafeLogProject.cafeLog.service;

import cafeLogProject.cafeLog.dto.RegistUserRequest;
import cafeLogProject.cafeLog.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public interface UserService {
    @Transactional
    User addUser(RegistUserRequest registUserRequest);
    User findUserById(long userId);
}
