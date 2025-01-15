package cafeLogProject.cafeLog.test.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class ReviewSaveRes {

    private Long userId;
    private Long cafeId;
    private Long reviewId;
    private String content;
    private int rating;
    private LocalDate visitDate;
    private List<Integer> tagIds;


    @Builder
    public ReviewSaveRes(Long userId, Long cafeId, Long reviewId, String content, int rating, LocalDate visitDate, List<Integer> tagIds) {

        this.userId = userId;
        this.cafeId = cafeId;
        this.reviewId = reviewId;
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.tagIds = tagIds;
    }
}
