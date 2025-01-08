package cafeLogProject.cafeLog.domains.cafe.repository;

import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeRepositoryCustom{

    Optional<Cafe> findByCafeNameAndMapxAndMapy(String cafeName, String mapx, String mapy);

}
