package cafeLogProject.cafeLog.domains.cafe.repository;

import cafeLogProject.cafeLog.api.cafe.dto.GetCafeInfoRes;

import java.util.Optional;

public interface CafeRepositoryCustom {

    Optional<GetCafeInfoRes> findCafeWithAverageRating(Long cafeId);
}
