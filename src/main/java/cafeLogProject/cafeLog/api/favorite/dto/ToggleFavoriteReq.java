package cafeLogProject.cafeLog.api.favorite.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ToggleFavoriteReq {

    @NotNull
    private Long cafeId;

    @NotNull
    private Boolean isScrap;
}
