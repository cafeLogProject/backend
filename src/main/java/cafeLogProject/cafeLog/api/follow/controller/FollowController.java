package cafeLogProject.cafeLog.api.follow.controller;

import cafeLogProject.cafeLog.api.follow.dto.FollowRes;
import cafeLogProject.cafeLog.api.follow.dto.UserFollowRes;
import cafeLogProject.cafeLog.api.follow.service.FollowService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow/{userId}")
    public ResponseEntity<FollowRes> followUser(@AuthenticationPrincipal CustomOAuth2User user,
                                                @PathVariable Long userId) {

        FollowRes followRes = followService.followUser(user.getName(), userId);
        return ResponseEntity.ok(followRes);
    }

    @DeleteMapping("/follow/{userId}")
    public ResponseEntity<FollowRes> unfollowUser(@AuthenticationPrincipal CustomOAuth2User user,
                                                @PathVariable Long userId) {

        FollowRes followRes = followService.unfollowUser(user.getName(), userId);
        return ResponseEntity.ok(followRes);
    }

    @GetMapping("/users/{userId}/follower")
    public ResponseEntity<List<UserFollowRes>> getFollowerList(@AuthenticationPrincipal CustomOAuth2User user,
                                                               @PathVariable Long userId,
                                                               @RequestParam(defaultValue = "10") int limit,
                                                               @RequestParam(required = false) Long cursor
                                                               ) {

        List<UserFollowRes> followerList = followService.getFollowerList(user.getName(), userId, limit, cursor);
        return ResponseEntity.ok(followerList);
    }

    @GetMapping("/users/{userId}/following")
    public ResponseEntity<List<UserFollowRes>> getFollowingList(@AuthenticationPrincipal CustomOAuth2User user,
                                                                @PathVariable Long userId,
                                                                @RequestParam(defaultValue = "10") int limit,
                                                                @RequestParam(required = false) Long cursor) {

        List<UserFollowRes> followingList = followService.getFollowingList(user.getName(), userId, limit, cursor);
        return ResponseEntity.ok(followingList);
    }
}
