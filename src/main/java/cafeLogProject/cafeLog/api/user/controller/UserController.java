package cafeLogProject.cafeLog.api.user.controller;

import cafeLogProject.cafeLog.api.user.dto.*;
import cafeLogProject.cafeLog.api.user.service.UserService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/my/profile")
    public ResponseEntity<UserInfoRes> getUserInfo(@AuthenticationPrincipal CustomOAuth2User user) {

        UserInfoRes userInfo = userService.getUserInfo(user.getName());
        return ResponseEntity.ok(userInfo);
    }

    @PatchMapping("/my/profile")
    public ResponseEntity<Void> updateUser(@AuthenticationPrincipal CustomOAuth2User user,
                                           @RequestBody @Valid UserUpdateReq userUpdateReq) {

        userService.updateUser(user.getName(), userUpdateReq);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/check")
    public ResponseEntity<IsExistNicknameRes> isExistNickname(@RequestParam @Valid String nickname,
                                                              @AuthenticationPrincipal CustomOAuth2User user) {

        IsExistNicknameRes isExist = userService.isExistNickname(user.getName(), nickname);
        return ResponseEntity.ok(isExist);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<OtherUserInfoRes> getOtherUserInfo(@AuthenticationPrincipal CustomOAuth2User user,
                                                             @PathVariable Long userId) {

        OtherUserInfoRes otherUserInfo = userService.getOtherUserInfo(user.getName(), userId);
        return ResponseEntity.ok(otherUserInfo);
    }

    @GetMapping("/profile/search")
    public ResponseEntity<List<UserSearchRes>> searchUserByNickname(@RequestParam String nickname,
                                                                    @AuthenticationPrincipal CustomOAuth2User user) {

        List<UserSearchRes> userSearchRes = userService.searchUserByNickname(nickname, user.getName());
        return ResponseEntity.ok(userSearchRes);
    }
}
