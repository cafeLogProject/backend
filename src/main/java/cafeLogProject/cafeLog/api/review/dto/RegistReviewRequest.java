package cafeLogProject.cafeLog.api.review.dto;


import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.user.domain.User;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;


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
    @Size(max = 5, message = "최대 태그 개수는 5입니다.")
    private List<Integer> tagIds;

    @Builder
    public RegistReviewRequest(String content, Integer rating, LocalDate visitDate, List<Integer> tagIds) {
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.tagIds = tagIds;
    }

    public Review toEntity(User user, Cafe cafe) {
        return Review.builder()
                .content(content)
                .rating(rating)
                .visitDate(visitDate)
                .cafe(cafe)
                .user(user)
                .build();
    }
}

