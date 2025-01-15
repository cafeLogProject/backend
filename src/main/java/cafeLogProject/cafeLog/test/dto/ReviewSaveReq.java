package cafeLogProject.cafeLog.test.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ReviewSaveReq {

    @NotNull(message = "카페ID는 필수 입력 값입니다.")
    private Long cafeId;

    private String content;

    @NotNull(message = "별점은 필수 입력 값입니다.")
    @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5 이하여야 합니다.")
    private int rating;

    @NotNull(message = "방문날짜는 필수 입력 값입니다.")
    private LocalDate visitDate;

    private List<Integer> tagIds;

}
