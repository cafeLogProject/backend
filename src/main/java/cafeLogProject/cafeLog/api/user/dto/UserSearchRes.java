package cafeLogProject.cafeLog.api.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSearchRes {

    private Long userId;
    private String nickname;
    private String introduce;

    @QueryProjection
    public UserSearchRes(Long userId, String nickname, String introduce) {

        this.userId = userId;
        this.nickname = nickname;
        this.introduce = introduce;
    }

}
