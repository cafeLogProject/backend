package cafeLogProject.cafeLog.api.draftReview.dto;

import cafeLogProject.cafeLog.domains.draftReview.domain.DraftReview;
import cafeLogProject.cafeLog.domains.user.domain.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
public class ShowDraftReviewResponse {
    private Long draftReviewId;
    private String content;
    private Integer rating;
    private LocalDate visitDate;
    private Set<UUID> imageIds = new HashSet<>();
    private Set<Integer> tagIds = new HashSet<>();
    private Long cafeId;
    private Long userId;

    @Builder
    @QueryProjection
    public ShowDraftReviewResponse(Long draftReviewId, String content, Integer rating, LocalDate visitDate, Set<UUID> imageIds, Set<Integer> tagIds, Long cafeId, Long userId) {
        this.draftReviewId = draftReviewId;
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.imageIds = imageIds;
        this.tagIds = tagIds;
        this.cafeId = cafeId;
        this.userId = userId;
    }

    @QueryProjection
    public ShowDraftReviewResponse(Long draftReviewId, String content, Integer rating, LocalDate visitDate, List<UUID> imageIds, List<Integer> tagIds, Long cafeId, Long userId) {
        this.draftReviewId = draftReviewId;
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.imageIds = new HashSet<>(imageIds);
        this.tagIds = new HashSet<>(tagIds);
        this.cafeId = cafeId;
        this.userId = userId;
    }
    @QueryProjection
    public ShowDraftReviewResponse(DraftReview draftReview) {
        this.draftReviewId = draftReview.getId();
        this.content = draftReview.getContent();
        this.rating = draftReview.getRating();
        this.visitDate = draftReview.getVisitDate();
        this.imageIds =  new HashSet<>(draftReview.getImageIds());
        this.tagIds = new HashSet<>(draftReview.getTagIds());
        this.cafeId = draftReview.getCafe().getId();
        this.userId = draftReview.getUser().getId();
    }


    // queryDsl에 사용
    // Tuple 사용시
    public ShowDraftReviewResponse(DraftReview draftReview, User user) {
        this.draftReviewId = draftReview.getId();
        this.content = draftReview.getContent();
        this.rating = draftReview.getRating();
        this.visitDate = draftReview.getVisitDate();
        this.tagIds = new HashSet<>(draftReview.getTagIds());
        this.cafeId = draftReview.getCafe().getId();
        this.userId = user.getId();
    }

    public void addTagId(Integer tagId) {
        this.tagIds.add(tagId);
    }

    public void addImageId(UUID imageId) {
        this.imageIds.add(imageId);
    }


}
