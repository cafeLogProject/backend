package cafeLogProject.cafeLog.service.Impl;

import cafeLogProject.cafeLog.dto.RegistCafeRequest;
import cafeLogProject.cafeLog.entity.Cafe;
import cafeLogProject.cafeLog.repository.CafeRepository;
import cafeLogProject.cafeLog.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {
    private final CafeRepository cafeRepository;

    @Override
    public Cafe addCafe(RegistCafeRequest registCafeRequest) {
        return cafeRepository.save(registCafeRequest.toEntity());
    }

    @Override
    public Cafe findCafeById(Long cafeId) {
        Optional<Cafe> cafeOptional = cafeRepository.findById(cafeId);
        if (cafeOptional.isEmpty()) return null;
        return cafeOptional.get();
    }
}
