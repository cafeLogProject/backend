package cafeLogProject.cafeLog.dto;


import cafeLogProject.cafeLog.entity.Cafe;
import cafeLogProject.cafeLog.entity.Review;
import cafeLogProject.cafeLog.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class RegistReviewRequest {
    private String content;
    private int rating;
    private LocalDate visitDate;
    private List<String> images = new ArrayList<>();
    private TagCategory tags;
    private Long cafeId;        //필수
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

