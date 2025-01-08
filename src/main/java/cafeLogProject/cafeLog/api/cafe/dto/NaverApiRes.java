package cafeLogProject.cafeLog.api.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NaverApiRes {

    private List<NaverCafeInfo> items;
}
