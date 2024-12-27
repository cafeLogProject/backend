package cafeLogProject.cafeLog.domains.review.dto;


import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class RegistReviewRequest {
    private String content;
    @NotBlank(message = "별점은 필수 입력 값입니다.")
    @Pattern(regexp="^[0-5]$", message="숫자만 입력할 수 있습니다.")
    private int rating;
    @NotBlank(message = "방문날짜는 필수 입력 값입니다.")
    private LocalDate visitDate;
    private List<String> images = new ArrayList<>();
    private TagCategory tags;
    @NotBlank(message = "카페ID는 필수 입력 값입니다.")
    private Long cafeId;        //필수
    @NotBlank(message = "카페명은 필수 입력 값입니다.")
    private String cafeName;

    @Builder
    public RegistReviewRequest(String content, int rating, LocalDate visitDate, List<String> images, TagCategory tags, Long cafeId, String cafeName) {
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.images = images;
        this.tags = tags;
        this.cafeId = cafeId;
        this.cafeName = cafeName;
    }

    public Review toEntity(User user, Cafe cafe) {
        LocalDateTime createdAt = LocalDateTime.now();
        return Review.builder()
                .content(content)
                .rating(rating)
                .visitDate(visitDate)
                .images(images)
                .tags(tags.getAllIds())
                .cafe(cafe)
                .user(user)
                .createdAt(createdAt)
                .build();
    }
}

