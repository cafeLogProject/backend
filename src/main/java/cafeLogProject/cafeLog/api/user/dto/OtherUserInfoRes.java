package cafeLogProject.cafeLog.api.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class OtherUserInfoRes {

    private Long userId;

    private String nickname;

    private String introduce;

    private String email;

    private Boolean isProfileImageExist;

    @JsonProperty("isFollow")
    private boolean isFollow;

    private int follower_cnt;

    private int following_cnt;

    private int review_cnt;

    @QueryProjection
    public OtherUserInfoRes(Long userId, String nickname, String introduce, String email, Boolean isProfileImageExist, boolean isFollow, int follower_cnt, int following_cnt, int review_cnt) {

        this.userId = userId;
        this.nickname = nickname;
        this.introduce = introduce;
        this.email = email;
        this.isProfileImageExist = isProfileImageExist;
        this.isFollow = isFollow;
        this.follower_cnt = follower_cnt;
        this.following_cnt = following_cnt;
        this.review_cnt = review_cnt;
    }
}
