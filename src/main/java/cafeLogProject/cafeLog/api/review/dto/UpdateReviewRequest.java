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
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Slf4j
public class UpdateReviewRequest {
    private String content;
    @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5 이하여야 합니다.")
    private Integer rating;
    private LocalDate visitDate;
    private List<String> imageIds;
    private List<Integer> tagIds;

    @Builder
    public UpdateReviewRequest(String content, int rating, LocalDate visitDate, List<String> imageIds, List<Integer> tagIds) {
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.imageIds = imageIds;
        this.tagIds = tagIds;
    }
    
    // 이미지 필드 존재하는 경우 주석 풀기
    // 이미지 변경사항 있는 경우
//    public Review toEntity(Review review, List<ReviewImage> images) {
//        this.content = !(this.content).equals("") ? this.content : review.getContent();
//        this.rating = this.rating != null ? this.rating : review.getRating();
//        this.visitDate = this.visitDate != null ? this.visitDate : review.getVisitDate();
//        List<Integer> tagIds = this.tags != null ? this.tags.findAllIds() : review.getTagIds();
//
//        return Review.builder()
//                .id(review.getId())
//                .content(content)
//                .rating(rating)
//                .visitDate(visitDate)
//                .images(images)
//                .tagIds(tagIds)
//                .cafe(review.getCafe())
//                .user(review.getUser())
//                .build();
//    }

    public Review toEntity(Review review) {
        log.error(content);
        this.content = this.content != null ? this.content : review.getContent();
        this.rating = this.rating != null ? this.rating : review.getRating();
        this.visitDate = this.visitDate != null ? this.visitDate : review.getVisitDate();
        List<Integer> tagIds = (this.tagIds != null) ? this.tagIds : review.getTagIds();

        return Review.builder()
                .id(review.getId())
                .content(content)
                .rating(rating)
                .visitDate(visitDate)
                .tagIds(tagIds)
                .cafe(review.getCafe())
                .user(review.getUser())
                .build();
    }
}
