package cafeLogProject.cafeLog.api.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;

@Data
public class UserInfoRes {

    private Long userId;

    private String nickname;

    private String introduce;

    private String email;

    private Boolean isProfileImageExist;

    @Builder
    public UserInfoRes(Long userId, String nickname, String introduce, String email, Boolean isProfileImageExist) {
        this.userId = userId;
        this.nickname = nickname;
        this.introduce = introduce;
        this.email = email;
        this.isProfileImageExist = isProfileImageExist;
    }
}
