package cafeLogProject.cafeLog.test.dto;

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
    private Integer rating;
    private LocalDate visitDate;
    private List<Integer> tagIds;

}
