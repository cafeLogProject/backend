package cafeLogProject.cafeLog.domains.follow.repository;

import cafeLogProject.cafeLog.api.follow.dto.UserFollowRes;
import cafeLogProject.cafeLog.domains.user.domain.User;

import java.util.List;

public interface FollowRepositoryCustom {

    void deleteFollow(User currentUser, User otherUser);
    List<UserFollowRes> getFollowerList(Long currentUserId, Long otherUserId, int limit, Long cursor);
    List<UserFollowRes> getFollowingList(Long currentUserId, Long otherUserId, int limit, Long cursor);

}
