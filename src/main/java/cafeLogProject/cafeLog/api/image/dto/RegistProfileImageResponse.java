package cafeLogProject.cafeLog.api.image.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistProfileImageResponse {
    String imageId;

    @Builder
    public RegistProfileImageResponse(String imageId) {
        this.imageId = imageId;
    }
}
