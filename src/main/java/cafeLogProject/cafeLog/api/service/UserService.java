package cafeLogProject.cafeLog.api.service;

import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.dto.RegistUserRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public interface UserService {
    @Transactional
    User addUser(RegistUserRequest registUserRequest);
    User findUserById(long userId);
}
