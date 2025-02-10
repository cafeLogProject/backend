package cafeLogProject.cafeLog.api.image.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistDraftReviewImageResponse {
    String imageId;

    @Builder
    public RegistDraftReviewImageResponse(String imageId) {
        this.imageId = imageId;
    }
}
