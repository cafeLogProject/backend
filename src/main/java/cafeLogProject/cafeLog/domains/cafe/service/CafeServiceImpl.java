package cafeLogProject.cafeLog.domains.cafe.service;

import cafeLogProject.cafeLog.domains.cafe.dto.RegistCafeRequest;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.cafe.exception.CafeNotFoundException;
import cafeLogProject.cafeLog.domains.cafe.exception.CafeSaveException;
import cafeLogProject.cafeLog.domains.cafe.repository.CafeRepository;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {
    private final CafeRepository cafeRepository;

    @Override
    public Cafe addCafe(RegistCafeRequest registCafeRequest) {
        try {
            return cafeRepository.save(registCafeRequest.toEntity());
        } catch (Exception e) {
            throw new CafeSaveException(ErrorCode.CAFE_SAVE_ERROR);
        }
    }

    @Override
    public Cafe findCafeById(Long cafeId) {
        Optional<Cafe> cafeOptional = cafeRepository.findById(cafeId);
        if (cafeOptional.isEmpty()) throw new CafeNotFoundException(ErrorCode.CAFE_NOT_FOUND_ERROR);
        return cafeOptional.get();
    }

    @Override
    public boolean existsByCafeNameAndLocationXY(String cafeName, List<Long> locationXY) {
        return existsByCafeNameAndLocationXY(cafeName, locationXY);
    }
}
