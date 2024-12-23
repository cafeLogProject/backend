package cafeLogProject.cafeLog.repository;

import cafeLogProject.cafeLog.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CafeRepository extends JpaRepository<Cafe, Long> {
    Optional<Cafe> findById(Long cafeId);
    Cafe save(Cafe cafe);
}
