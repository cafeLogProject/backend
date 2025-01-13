package cafeLogProject.cafeLog.domains.user.repository;

import cafeLogProject.cafeLog.domains.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);
    User save(User user);
    Optional<User> findByUsername(String username);
    Boolean existsByNickname(String nickname);
}
