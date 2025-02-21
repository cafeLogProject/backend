package cafeLogProject.cafeLog.api.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSearchRes {

    private Long userId;

    private String nickname;

    private Boolean isProfileImageExist;

    @JsonProperty("isFollow")
    private int isFollow;

    private String followerCountMessage;

    @QueryProjection
    public UserSearchRes(Long userId, String nickname, Boolean isProfileImageExist) {

        this.userId = userId;
        this.nickname = nickname;
        this.isProfileImageExist = isProfileImageExist;
    }

}
