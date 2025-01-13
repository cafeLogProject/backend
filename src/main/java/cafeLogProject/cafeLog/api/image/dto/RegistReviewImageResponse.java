package cafeLogProject.cafeLog.api.image.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistReviewImageResponse {
    String imageId;

    @Builder
    public RegistReviewImageResponse(String imageId) {
        this.imageId = imageId;
    }
}
