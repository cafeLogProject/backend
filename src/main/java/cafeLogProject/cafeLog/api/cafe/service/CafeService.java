package cafeLogProject.cafeLog.api.cafe.service;

import cafeLogProject.cafeLog.api.cafe.dto.RegistCafeRequest;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.common.exception.cafe.CafeNotFoundException;
import cafeLogProject.cafeLog.common.exception.cafe.CafeSaveException;
import cafeLogProject.cafeLog.domains.cafe.repository.CafeRepository;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CafeService {
    private final CafeRepository cafeRepository;

    @Transactional
    public Cafe addCafe(RegistCafeRequest registCafeRequest) {
        try {
            return cafeRepository.save(registCafeRequest.toEntity());
        } catch (Exception e) {
            throw new CafeSaveException(ErrorCode.CAFE_SAVE_ERROR);
        }
    }

    public Cafe findCafeById(Long cafeId) {
        Optional<Cafe> cafeOptional = cafeRepository.findById(cafeId);
        if (cafeOptional.isEmpty()) throw new CafeNotFoundException(Long.toString(cafeId), ErrorCode.CAFE_NOT_FOUND_ERROR);
        return cafeOptional.get();
    }

    public boolean existsByCafeNameAndLocationXY(String cafeName, List<Long> locationXY) {
        return existsByCafeNameAndLocationXY(cafeName, locationXY);
    }
}
