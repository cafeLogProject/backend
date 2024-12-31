package cafeLogProject.cafeLog.domains.cafe.repository;

import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CafeRepository extends JpaRepository<Cafe, Long> {
    Optional<Cafe> findById(Long cafeId);
    boolean existsByCafeNameAndLocationXY(String cafeName, List<Long> locationXY);
}
