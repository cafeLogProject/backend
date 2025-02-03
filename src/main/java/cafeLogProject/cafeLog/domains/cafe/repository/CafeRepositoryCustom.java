package cafeLogProject.cafeLog.domains.cafe.repository;

import cafeLogProject.cafeLog.api.cafe.dto.CafeInfoRes;

import java.util.Optional;

public interface CafeRepositoryCustom {

    Optional<CafeInfoRes> findCafeWithAverageRating(Long cafeId, String username);
}
