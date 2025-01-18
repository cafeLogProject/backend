package cafeLogProject.cafeLog.api.review.dto;

import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.user.domain.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
public class ShowReviewResponse {
    private Long reviewId;
    private String content;
    private Integer rating;
    private LocalDate visitDate;
    private Set<UUID> imageIds = new HashSet<>();
    private Set<Integer> tagIds = new HashSet<>();
    private Long cafeId;
    private Long userId;
    private String nickname;
    private Boolean isProfileImageExist;
    private LocalDateTime createdAt;

    @Builder
    public ShowReviewResponse(Long reviewId, String content, Integer rating, LocalDate visitDate, List<UUID> imageIds, List<Integer> tagIds, Long cafeId, Long userId, String nickname, Boolean isImageExist, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
//        this.imageIds = imageIds;
//        this.tagIds = tagIds;
        this.cafeId = cafeId;
        this.userId = userId;
        this.nickname = nickname;
        this.isProfileImageExist = isImageExist;
        this.createdAt = createdAt;
    }

    // queryDsl에 사용
    @QueryProjection
    public ShowReviewResponse(Review review, Set<UUID> imageIds, Set<Integer> tagIds) {
        this.reviewId = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.visitDate = review.getVisitDate();
        this.imageIds = imageIds;
        this.tagIds = tagIds;
        this.cafeId = review.getCafe().getId();
        this.userId = review.getUser().getId();
        this.nickname = review.getUser().getNickname();
        this.isProfileImageExist = review.getUser().isImageExist();
        this.createdAt = review.getCreatedAt();
    }


    // queryDsl에 사용
//    @QueryProjection
//    public ShowReviewResponse(Review review, List<UUID> imageIds, List<Integer> tagIds) {
//        this.reviewId = review.getId();
//        this.content = review.getContent();
//        this.rating = review.getRating();
//        this.visitDate = review.getVisitDate();
//        this.imageIds = imageIds;
//        this.tagIds = tagIds;
//        this.cafeId = review.getCafe().getId();
//        this.userId = review.getUser().getId();
//        this.nickname = review.getUser().getNickname();
//        this.isProfileImageExist = review.getUser().isImageExist();
//        this.createdAt = review.getCreatedAt();
//    }


    //Tuple 사용시
    public ShowReviewResponse(Review review, User user) {
        this.reviewId = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.visitDate = review.getVisitDate();
        this.cafeId = review.getCafe().getId();
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.isProfileImageExist = user.isImageExist();
        this.createdAt = review.getCreatedAt();
    }

    public void addTagId(Integer tagId) {
        this.tagIds.add(tagId);
    }

    public void addImageId(UUID imageId) {
        this.imageIds.add(imageId);
    }


}
