package cafeLogProject.cafeLog.api.user.controller;

import cafeLogProject.cafeLog.api.user.dto.UserUpdateReq;
import cafeLogProject.cafeLog.api.user.service.UserService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/my/profile")
    public ResponseEntity<Void> updateUser(@AuthenticationPrincipal CustomOAuth2User user,
                                           @RequestBody @Valid UserUpdateReq userUpdateReq) {

        userService.updateUser(user.getName(), userUpdateReq);

        return ResponseEntity.noContent().build();
    }

}
