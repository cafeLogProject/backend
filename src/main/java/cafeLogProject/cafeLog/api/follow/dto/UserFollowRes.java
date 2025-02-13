package cafeLogProject.cafeLog.api.follow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;


@Data
public class UserFollowRes {

    private Long userId;

    private String nickname;

    private Boolean isProfileImageExist;

    private int follower_cnt;

    private int review_cnt;

    @JsonProperty("isFollow")
    private int isFollow;

    private Long followId;

    @QueryProjection
    public UserFollowRes(Long userId, String nickname, Boolean isProfileImageExist, int follower_cnt, int review_cnt, Long followId) {

        this.userId = userId;
        this.nickname = nickname;
        this.isProfileImageExist = isProfileImageExist;
        this.follower_cnt = follower_cnt;
        this.review_cnt = review_cnt;
        this.followId = followId;
    }
}
