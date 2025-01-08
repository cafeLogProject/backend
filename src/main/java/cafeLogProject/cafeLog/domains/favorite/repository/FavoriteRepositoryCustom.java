package cafeLogProject.cafeLog.domains.favorite.repository;

import cafeLogProject.cafeLog.api.favorite.dto.FavoriteCafeInfo;

import java.util.List;

public interface FavoriteRepositoryCustom {

    void deleteFavorite(String username, Long cafeId);

    boolean isExistFavorite(String username, Long cafeId);

    List<FavoriteCafeInfo> findMyFavoriteCafes(String username);
}
