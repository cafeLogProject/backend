package cafeLogProject.cafeLog.api.review.dto;

import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.UnexpectedServerException;
import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateReviewRequest {
    private String content;
    @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5 이하여야 합니다.")
    private Integer rating;
    private LocalDate visitDate;
    private List<String> imageIds;
    private TagCategory tags;

    @Builder
    public UpdateReviewRequest(String content, int rating, LocalDate visitDate, List<String> imageIds, TagCategory tags) {
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.imageIds = imageIds;
        this.tags = tags;
    }
    
    // 이미지 변경사항 있는 경우
    public Review toEntity(Review review, List<ReviewImage> images) {
        this.content = !(this.content).equals("") ? this.content : review.getContent();
        this.rating = this.rating != null ? this.rating : review.getRating();
        this.visitDate = this.visitDate != null ? this.visitDate : review.getVisitDate();
        List<Integer> tagIds = this.tags != null ? this.tags.findAllIds() : review.getTagIds();

        return Review.builder()
                .id(review.getId())
                .content(content)
                .rating(rating)
                .visitDate(visitDate)
                .images(images)
                .tagIds(tagIds)
                .cafe(review.getCafe())
                .user(review.getUser())
                .build();
    }

    // 이미지 변경사항 없는 경우
    public Review toEntity(Review review) {
        this.content = !(this.content).equals("") ? this.content : review.getContent();
        this.rating = this.rating != null ? this.rating : review.getRating();
        this.visitDate = this.visitDate != null ? this.visitDate : review.getVisitDate();
        List<Integer> tagIds = this.tags != null ? this.tags.findAllIds() : review.getTagIds();
        
        List<ReviewImage> oldImages = review.getImages();
        return Review.builder()
                .id(review.getId())
                .content(content)
                .rating(rating)
                .visitDate(visitDate)
                .images(oldImages)
                .tagIds(tagIds)
                .cafe(review.getCafe())
                .user(review.getUser())
                .build();
    }
}
