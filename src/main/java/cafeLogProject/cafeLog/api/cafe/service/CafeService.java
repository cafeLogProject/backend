package cafeLogProject.cafeLog.api.cafe.service;

import cafeLogProject.cafeLog.api.cafe.dto.CafeInfoRes;
import cafeLogProject.cafeLog.api.cafe.dto.IsExistCafeRes;
import cafeLogProject.cafeLog.api.cafe.dto.SaveCafeReq;
import cafeLogProject.cafeLog.common.exception.cafe.CafeCategoryException;
import cafeLogProject.cafeLog.common.exception.cafe.CafeNotFoundException;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static cafeLogProject.cafeLog.common.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {
    private final CafeRepository cafeRepository;

    public CafeInfoRes getCafeInfo(Long cafeId) {

        return cafeRepository.findCafeWithAverageRating(cafeId)
                .orElseThrow(() -> new CafeNotFoundException(CAFE_NOT_FOUND_ERROR));
    }

    @Transactional
    public Long saveCafe(SaveCafeReq cafeReq) {

        validateCafeCategory(cafeReq);
        Cafe savedCafe = cafeRepository.save(cafeReq.toEntity());
        log.info("Cafe saved successfully : {}", savedCafe.getId());

        return savedCafe.getId();
    }

    public IsExistCafeRes isExistCafe(String name, String mapx, String mapy) {

        Optional<Cafe> existCafe = cafeRepository.findByCafeNameAndMapxAndMapy(name, mapx, mapy);

        return existCafe
                .map(cafe -> new IsExistCafeRes(cafe.getId(), true))
                .orElseGet(() -> new IsExistCafeRes(null, false));
    }


    private void validateCafeCategory(SaveCafeReq cafeReq) {

        if (!cafeReq.getCategory().contains("카페")){
            throw new CafeCategoryException(CAFE_CATEGORY_ERROR);
        }
    }

}
