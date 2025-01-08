package cafeLogProject.cafeLog.api.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IsExistCafeRes {

    private Long cafeId;
    private boolean isExist;
}
