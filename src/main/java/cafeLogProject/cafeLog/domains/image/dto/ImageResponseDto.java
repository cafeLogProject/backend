package cafeLogProject.cafeLog.domains.image.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@NoArgsConstructor
public class ImageResponseDto {
    private Resource imageFile;
    //UUID로 생성됨
    private String imageId;

    @Builder
    public ImageResponseDto (Resource imageFile, String imageId) {
        this.imageFile = imageFile;
        this.imageId = imageId;
    }
}
