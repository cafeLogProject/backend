package cafeLogProject.cafeLog.api.cafe.dto;


import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RegistCafeRequest {
    @NotBlank(message = "카페명은 필수 입력 값입니다.")
    private String cafeName;
    @NotBlank(message = "장소명은 필수 입력 값입니다.")
    private String locationStr;
    @NotEmpty(message = "장소XY값은 필수 입력 값입니다.")
    private List<Long> locationXY = new ArrayList<>();
    private boolean isClosedDown = false;
    private String etcLink;

    @Builder
    public RegistCafeRequest (String cafeName, String locationStr, boolean isClosedDown, List<Long> locationXY, String etcLink) {
        this.cafeName = cafeName;
        this.locationStr = locationStr;
        this.locationXY.add(locationXY.get(0));
        this.locationXY.add(locationXY.get(1));
        this.isClosedDown = isClosedDown;
        this.etcLink = etcLink;
    }

    public Cafe toEntity() {
        return Cafe.builder()
                .cafeName(cafeName)
                .locationStr(locationStr)
                .locationXY(locationXY)
                .isClosedDown(isClosedDown)
                .etcLink(etcLink)
                .build();
    }
}
