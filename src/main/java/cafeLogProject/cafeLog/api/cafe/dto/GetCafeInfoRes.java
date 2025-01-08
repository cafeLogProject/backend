package cafeLogProject.cafeLog.api.cafe.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class GetCafeInfoRes {

    private String cafeName;
    private String address;
    private String roadAddress;
    private String mapx;
    private String mapy;
    private String link;
    private boolean is_closed_down;
    private long avgRating;

    @QueryProjection
    public GetCafeInfoRes(String cafeName, String address, String roadAddress, String mapx, String mapy, String link, boolean is_closed_down, long avgRating) {
        this.cafeName = cafeName;
        this.address = address;
        this.roadAddress = roadAddress;
        this.mapx = mapx;
        this.mapy = mapy;
        this.link = link;
        this.is_closed_down = is_closed_down;
        this.avgRating = avgRating;
    }
}
