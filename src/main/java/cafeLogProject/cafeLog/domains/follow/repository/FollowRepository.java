package cafeLogProject.cafeLog.domains.follow.repository;

import cafeLogProject.cafeLog.domains.follow.domain.Follow;
import cafeLogProject.cafeLog.domains.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {

    boolean existsByFollowerAndFollowing(User follower, User following);
}
