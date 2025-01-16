package cafeLogProject.cafeLog.api.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class UserInfoRes {


    private String nickname;

    private String introduce;

    @Builder
    public UserInfoRes(String nickname, String introduce) {

        this.nickname = nickname;
        this.introduce = introduce;
    }
}
