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
    private String cafeName;
    private Long userId;
    private String nickname;
    private Boolean isProfileImageExist;
    private LocalDateTime createdAt;

    @Builder
    public ShowReviewResponse(Review review, List<UUID> imageIds, List<Integer> tagIds){
        this.reviewId = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.visitDate = review.getVisitDate();
        this.imageIds = new HashSet<>(imageIds);
        this.tagIds = new HashSet<>(tagIds);
        this.cafeId = review.getCafe().getId();
        this.userId = review.getUser().getId();
        this.nickname = review.getUser().getNickname();
        this.isProfileImageExist = review.getUser().isImageExist();
        this.createdAt = review.getCreatedAt();
    }

    @QueryProjection
    public ShowReviewResponse(Long reviewId, String content, Integer rating, LocalDate visitDate, Set<UUID> imageIds, Set<Integer> tagIds, Long cafeId, String cafeName, Long userId, String nickname, Boolean isImageExist, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.imageIds = imageIds;
        this.tagIds = tagIds;
        this.cafeId = cafeId;
        this.cafeName = cafeName;
        this.userId = userId;
        this.nickname = nickname;
        this.isProfileImageExist = isImageExist;
        this.createdAt = createdAt;
    }

    // queryDsl에 사용
    // queryDsl의 Transformer 사용시
    @QueryProjection
    public ShowReviewResponse(Review review, Long cafeId, String cafeName, Set<UUID> imageIds, Set<Integer> tagIds) {
        this.reviewId = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.visitDate = review.getVisitDate();
        this.imageIds = imageIds;
        this.tagIds = tagIds;
        this.cafeId = cafeId;
        this.cafeName = cafeName;
        this.userId = review.getUser().getId();
        this.nickname = review.getUser().getNickname();
        this.isProfileImageExist = review.getUser().isImageExist();
        this.createdAt = review.getCreatedAt();
    }


    // queryDsl에 사용
    // Tuple 사용시
    public ShowReviewResponse(Review review, User user) {
        this.reviewId = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.visitDate = review.getVisitDate();
        this.cafeId = review.getCafe().getId();
        this.cafeName = review.getCafe().getCafeName();
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
