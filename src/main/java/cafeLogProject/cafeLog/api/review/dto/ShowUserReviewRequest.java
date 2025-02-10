package cafeLogProject.cafeLog.api.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowUserReviewRequest {
    @Min(value = 1, message = "최소 리뷰 개수는 1입니다.")
    @Max(value = 20, message = "최대 리뷰 개수는 20입니다.")
    Integer limit;

    LocalDateTime timestamp;

    public ShowUserReviewRequest() {
        limit = 10;
        timestamp = LocalDateTime.of(3000,1,1,0,0,0,0);
    }

    @Builder
    public ShowUserReviewRequest(Integer limit, LocalDateTime timestamp) {
        this.limit = limit;
        this.timestamp = timestamp;
    }
}
