package cafeLogProject.cafeLog.api.review.dto;

import cafeLogProject.cafeLog.domains.review.domain.Review;
import lombok.Builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShowReviewResponse {
    private Long reviewId;
    private String content;
    private Integer rating;
    private LocalDate visitDate;
    private List<String> imageIds = new ArrayList<>();
    private TagCategory tags;
    private Long cafeId;
    private Long userId;

    @Builder
    public ShowReviewResponse(Long reviewId, String content, Integer rating, LocalDate visitDate, List<String> imageIds, TagCategory tags, Long cafeId, Long userId) {
        this.reviewId = reviewId;
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.imageIds = imageIds;
        this.tags = tags;
        this.cafeId = cafeId;
        this.userId = userId;
    }

    public ShowReviewResponse(Review review){
        this.reviewId = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.visitDate = review.getVisitDate();
        this.imageIds = review.getImageIds();
        this.tags = new TagCategory(review.getTagIds());
        this.cafeId = review.getCafe().getId();
        this.userId = review.getUser().getId();
    }
}
