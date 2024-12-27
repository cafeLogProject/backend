package cafeLogProject.cafeLog.domains.cafe.service;

import cafeLogProject.cafeLog.domains.cafe.dto.RegistCafeRequest;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface CafeService {
    @Transactional
    Cafe addCafe(RegistCafeRequest registCafeRequest);
    Cafe findCafeById(Long cafeId);
    boolean existsByCafeNameAndLocationXY(String CafeName, List<Long> locationXY);
}
