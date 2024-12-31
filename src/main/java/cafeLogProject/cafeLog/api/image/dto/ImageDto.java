package cafeLogProject.cafeLog.api.image.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class ImageDto {
    @NotBlank
    private MultipartFile file;

    @Builder
    public ImageDto(MultipartFile file) {
        this.file = file;
    }

}
