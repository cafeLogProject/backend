package cafeLogProject.cafeLog.common.runner;

import cafeLogProject.cafeLog.api.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowDataInitializer {

    private final FollowService followService;

    public void initialize() {
        log.info("Initializing follow relationships...");
        createFollowRelationships();
        log.info("Follow relationships initialization completed");
    }

    private void createFollowRelationships() {
        try {
            // user1의 팔로잉
            followService.followUser("google_g111111", 2L);
            followService.followUser("google_g111111", 3L);
            followService.followUser("google_g111111", 4L);
            followService.followUser("google_g111111", 5L);
            followService.followUser("google_g111111", 20L);

            // user2의 팔로잉
            followService.followUser("google_g123456", 1L);
            followService.followUser("google_g123456", 3L);
            followService.followUser("google_g123456", 6L);

            // user3의 팔로잉
            followService.followUser("google_g234567", 1L);
            followService.followUser("google_g234567", 4L);
            followService.followUser("google_g234567", 7L);
            followService.followUser("google_g234567", 8L);

            // user4의 팔로잉
            followService.followUser("google_g345678", 2L);
            followService.followUser("google_g345678", 5L);
            followService.followUser("google_g345678", 6L);

            // user5를 팔로우하는 사용자들
            followService.followUser("google_n112233", 5L);
            followService.followUser("google_n223344", 5L);
            followService.followUser("google_n334455", 5L);
            followService.followUser("google_n445566", 5L);
            followService.followUser("google_f123123", 5L);

            // user6의 팔로잉
            followService.followUser("google_n112233", 7L);
            followService.followUser("google_n112233", 8L);

            // user7의 팔로잉
            followService.followUser("google_n223344", 9L);
            followService.followUser("google_n223344", 10L);

            // user8의 팔로잉
            followService.followUser("google_n334455", 10L);
            followService.followUser("google_n334455", 11L);

            // user9의 팔로잉
            followService.followUser("google_n445566", 12L);
            followService.followUser("google_n445566", 13L);

            // user10의 팔로잉
            followService.followUser("google_f123123", 14L);
            followService.followUser("google_f123123", 15L);

            // user11 ~ user15의 팔로잉
            followService.followUser("google_f234234", 16L);
            followService.followUser("google_f234234", 17L);
            followService.followUser("google_g567890", 18L);
            followService.followUser("google_g567890", 19L);
            followService.followUser("google_g678901", 19L);
            followService.followUser("google_g678901", 20L);
            followService.followUser("google_n556677", 1L);
            followService.followUser("google_n556677", 2L);
            followService.followUser("google_n667788", 3L);
            followService.followUser("google_n667788", 4L);

            // user16 ~ user20의 팔로잉
            followService.followUser("google_f345345", 17L);
            followService.followUser("google_g789012", 16L);
            followService.followUser("google_n778899", 19L);
            followService.followUser("google_f456456", 18L);
            followService.followUser("google_g890123", 1L);

            log.info("Successfully created all follow relationships");
        } catch (Exception e) {
            log.error("Error creating follow relationships: {}", e.getMessage());
        }
    }
}