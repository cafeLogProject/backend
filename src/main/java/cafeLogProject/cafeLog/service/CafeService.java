package cafeLogProject.cafeLog.service;

import cafeLogProject.cafeLog.dto.RegistCafeRequest;
import cafeLogProject.cafeLog.entity.Cafe;
import cafeLogProject.cafeLog.repository.CafeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public interface CafeService {
    @Transactional
    Cafe addCafe(RegistCafeRequest registCafeRequest);
    Cafe findCafeById(Long cafeId);
}
