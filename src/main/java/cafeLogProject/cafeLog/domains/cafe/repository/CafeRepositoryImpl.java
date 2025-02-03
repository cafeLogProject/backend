package cafeLogProject.cafeLog.domains.cafe.repository;

import cafeLogProject.cafeLog.api.cafe.dto.CafeInfoRes;
import cafeLogProject.cafeLog.api.cafe.dto.QCafeInfoRes;
import cafeLogProject.cafeLog.domains.favorite.domain.QFavorite;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static cafeLogProject.cafeLog.domains.cafe.domain.QCafe.cafe;
import static cafeLogProject.cafeLog.domains.favorite.domain.QFavorite.*;
import static cafeLogProject.cafeLog.domains.review.domain.QReview.review;

@RequiredArgsConstructor
public class CafeRepositoryImpl implements CafeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CafeInfoRes> findCafeWithAverageRating(Long cafeId, String username) {


        CafeInfoRes result = queryFactory
                .select(new QCafeInfoRes(
                        cafe.cafeName,
                        cafe.address,
                        cafe.roadAddress,
                        cafe.mapx,
                        cafe.mapy,
                        cafe.link,
                        cafe.isClosedDown,
                        review.rating.avg().doubleValue()
                ))
                .from(cafe)
                .leftJoin(review).on(review.cafe.id.eq(cafeId))
                .where(cafe.id.eq(cafeId))
                .fetchOne();

        boolean isScrap = queryFactory
                .selectFrom(favorite)
                .where(favorite.cafe.id.eq(cafeId)
                        .and(favorite.user.username.eq(username)))
                .fetchOne() != null;

        result.setScrap(isScrap);
        return Optional.ofNullable(result).filter(r -> r.getCafeName() != null);
    }
}
