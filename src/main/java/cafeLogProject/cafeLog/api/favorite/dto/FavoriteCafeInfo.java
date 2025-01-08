package cafeLogProject.cafeLog.api.favorite.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FavoriteCafeInfo {

    private Long cafeId;
    private String cafeName;
    private String location;

    @QueryProjection
    public FavoriteCafeInfo(Long cafeId, String cafeName, String location) {
        this.cafeId = cafeId;
        this.cafeName = cafeName;
        this.location = location;
    }
}
