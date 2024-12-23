package cafeLogProject.cafeLog.dto;


import cafeLogProject.cafeLog.entity.Cafe;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistCafeRequest {
    private String cafename;
    private String location;
    private double avgStar = 0;
    private boolean isClosedDown = false;

    @Builder
    public RegistCafeRequest (String cafename, String location, double avgStar, boolean isClosedDown) {
        this.cafename = cafename;
        this.location = location;
        this.avgStar = avgStar;
        this.isClosedDown = isClosedDown;
    }

    public Cafe toEntity() {
        return Cafe.builder()
                .cafename(cafename)
                .location(location)
                .avgStar((float)avgStar)
                .isClosedDown(isClosedDown)
                .build();
    }
}
