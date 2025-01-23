package cafeLogProject.cafeLog.api.draftReview.dto;

import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.draftReview.domain.DraftReview;
import cafeLogProject.cafeLog.domains.user.domain.User;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class RegistDraftReviewRequest {
    @NotNull(message = "카페ID는 필수 입력 값입니다.")
    private Long cafeId;

    @Builder
    public RegistDraftReviewRequest(Long cafeId) {
        this.cafeId = cafeId;
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

    public DraftReview toEntity(User user, Cafe cafe) {
        return DraftReview.builder()
                .cafe(cafe)
                .user(user)
                .build();
    }
}

