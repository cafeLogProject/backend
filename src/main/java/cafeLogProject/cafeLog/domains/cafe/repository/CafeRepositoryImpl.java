package cafeLogProject.cafeLog.domains.cafe.repository;

import cafeLogProject.cafeLog.api.cafe.dto.GetCafeInfoRes;
import cafeLogProject.cafeLog.api.cafe.dto.QGetCafeInfoRes;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static cafeLogProject.cafeLog.domains.cafe.domain.QCafe.cafe;
import static cafeLogProject.cafeLog.domains.review.domain.QReview.review;

@RequiredArgsConstructor
public class CafeRepositoryImpl implements CafeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<GetCafeInfoRes> findCafeWithAverageRating(Long cafeId) {

        GetCafeInfoRes result = queryFactory
                .select(new QGetCafeInfoRes(
                        cafe.cafeName,
                        cafe.address,
                        cafe.roadAddress,
                        cafe.mapx,
                        cafe.mapy,
                        cafe.link,
                        cafe.isClosedDown,
                        review.rating.avg().longValue()
                ))
                .from(cafe)
                .leftJoin(review).on(review.cafe.id.eq(cafeId))
                .where(cafe.id.eq(cafeId))
                .fetchOne();

        return Optional.ofNullable(result).filter(r -> r.getCafeName() != null);
    }
}
