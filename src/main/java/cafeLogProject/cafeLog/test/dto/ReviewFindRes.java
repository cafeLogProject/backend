package cafeLogProject.cafeLog.test.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ReviewFindRes {

    private Long cafeId;
    private Long userId;
    private Long reviewId;

    private String content;
    private int rating;
    private LocalDate localDate;

    private String address;

    private List<Integer> tagIds;

    @QueryProjection
    public ReviewFindRes(Long cafeId, Long userId, Long reviewId, String content, int rating, LocalDate localDate, String address, List<Integer> tagIds) {
        this.cafeId = cafeId;
        this.userId = userId;
        this.reviewId = reviewId;
        this.content = content;
        this.rating = rating;
        this.localDate = localDate;
        this.address = address;
        this.tagIds = tagIds;
    }
}
