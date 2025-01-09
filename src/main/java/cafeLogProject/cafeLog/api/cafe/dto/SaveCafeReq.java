package cafeLogProject.cafeLog.api.cafe.dto;

import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveCafeReq {

    @NotNull(message = "카페 이름을 입력해주세요.")
    private String title;
    @NotNull(message = "카테고리를 입력해주세요.")
    private String category;
    @NotNull(message = "x좌표를 입력해주세요.")
    private String mapx;
    @NotNull(message = "y좌표를 입력해주세요.")
    private String mapy;
    @NotNull(message = "주소를 입력해주세요.")
    private String address;
    @NotNull(message = "도로명 주소를 입력해주세요.")
    private String roadAddress;
    private String link;

    public Cafe toEntity() {
        return Cafe.builder()
                .cafeName(this.title.replaceAll("<b>|</b>", ""))
                .address(this.address)
                .roadAddress(this.roadAddress)
                .mapx(this.mapx)
                .mapy(this.mapy)
                .link(this.link)
                .build();
    }
}
