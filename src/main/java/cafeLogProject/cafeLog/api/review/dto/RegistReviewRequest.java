package cafeLogProject.cafeLog.api.review.dto;


import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class RegistReviewRequest {
    private String content;
    @NotNull(message = "별점은 필수 입력 값입니다.")
    @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5 이하여야 합니다.")
    private Integer rating;
    @NotNull(message = "방문날짜는 필수 입력 값입니다.")
    private LocalDate visitDate;
    private List<String> imageIds = new ArrayList<>();
    private TagCategory tags;
    @NotNull(message = "카페ID는 필수 입력 값입니다.")
    private Long cafeId;        //필수

    @Builder
    public RegistReviewRequest(String content, Integer rating, LocalDate visitDate, List<String> imageIds, TagCategory tags, Long cafeId) {
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.imageIds = imageIds;
        this.tags = tags;
        this.cafeId = cafeId;
    }

    public Review toEntity(User user, Cafe cafe) {
        return Review.builder()
                .content(content)
                .rating(rating)
                .visitDate(visitDate)
                .tagIds(tags.findAllIds())
                .cafe(cafe)
                .user(user)
                .build();
    }
}

