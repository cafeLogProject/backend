package cafeLogProject.cafeLog.api.review.dto;


import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class RegistReviewRequest {
    private String content;
    @NotBlank(message = "별점은 필수 입력 값입니다.")

    @Pattern(regexp="^[0-5]$", message="숫자만 입력할 수 있습니다.")
    private Integer rating;
    @NotBlank(message = "방문날짜는 필수 입력 값입니다.")
    private LocalDate visitDate;
    private List<String> imageIds = new ArrayList<>();
    private TagCategory tags;
    @NotBlank(message = "카페ID는 필수 입력 값입니다.")
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
                .imageIds(imageIds)
                .tagIds(tags.getAllIds())
                .cafe(cafe)
                .user(user)
                .build();
    }
}

