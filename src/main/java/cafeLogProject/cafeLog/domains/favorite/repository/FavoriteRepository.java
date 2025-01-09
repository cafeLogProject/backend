package cafeLogProject.cafeLog.domains.favorite.repository;

import cafeLogProject.cafeLog.domains.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {

}
