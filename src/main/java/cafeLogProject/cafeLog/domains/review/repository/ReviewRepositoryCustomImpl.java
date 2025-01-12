package cafeLogProject.cafeLog.domains.review.repository;

import cafeLogProject.cafeLog.api.review.dto.QShowReviewResponse;
import cafeLogProject.cafeLog.api.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.review.ReviewInvalidSortError;
import cafeLogProject.cafeLog.common.exception.review.TagNotFoundException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cafeLogProject.cafeLog.domains.image.domain.QReviewImage.reviewImage;
import static cafeLogProject.cafeLog.domains.review.domain.QReview.review;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;


@RequiredArgsConstructor
@Slf4j
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    
    // 서브쿼리 대신 그룹화하고 변환하는 방법 (= transform) 사용
    @Override
    public ShowReviewResponse findShowReviewResponseById(Long reviewId) {
        return queryFactory
                .from(review)
                .leftJoin(reviewImage).on(review.id.eq(reviewImage.review.id))
                .where(review.id.eq(reviewId))
                .transform(
                        groupBy(review.id).as(
                                new QShowReviewResponse(
                                        review,
                                        list(reviewImage.id)    // 이미지 ID를 리스트로 변환
                                )
                        )
                )
                .get(reviewId);
    }

    @Override
    public List<ShowReviewResponse> searchByCafeId(Long cafeId, LocalDateTime createdAt, Pageable pageable) {
        return queryFactory
                .from(review)
                .leftJoin(reviewImage).on(review.id.eq(reviewImage.review.id))
                .where(review.cafe.id.eq(cafeId), isBeforeCreatedAt(createdAt))
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(review.id).list(
                                new QShowReviewResponse(
                                        review,
                                        list(reviewImage.id)    // 이미지 ID를 리스트로 변환
                                )
                        )
                );
    }

    // 최적화 필요 : 한 리뷰 당 태그 조회 쿼리 한번 더 나가는 문제
                // qeuryDsl는 @JoinColumn 적용 자동으로 안해줌
                    // -> 자동으로 join되지 않아 수동으로 join해줘야 함
                        // but Collection은 Q클래스가 생성되지 않음
    @Override
    public List<ShowReviewResponse> search(String sortMethod, List<Integer> tagIds, Integer currentRating, LocalDateTime createdAt, Pageable pageable) {
        OrderSpecifier[] orderSpecifiers = createOrderSpecifier(sortMethod);

        return queryFactory
                .from(review)
                .leftJoin(reviewImage).on(review.id.eq(reviewImage.review.id))
                .where(
                        eqTags(tagIds), isLowerThenRating(currentRating), isBeforeCreatedAt(createdAt)
                )
                .orderBy(
                        orderSpecifiers
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(review.id).list(
                                new QShowReviewResponse(
                                        review,
                                        list(reviewImage.id)    // 이미지 ID를 리스트로 변환
                                )
                        )
                );
    }

    private OrderSpecifier[] createOrderSpecifier(String sortMethod){
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        switch (sortMethod) {
            case "NEW" :
                orderSpecifiers.add(new OrderSpecifier(Order.DESC, review.createdAt));
                return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
            case "HIGH_RATING" :
                orderSpecifiers.add(new OrderSpecifier(Order.DESC, review.rating));
                orderSpecifiers.add(new OrderSpecifier(Order.DESC, review.createdAt));
                return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
            default:
                throw new ReviewInvalidSortError(ErrorCode.REVIEW_INVALID_SORT_ERROR);
        }
    }

    // 엔티티형으로 출력하는 경우
//    @Override
//    public List<Review> searchEntity(String sortMethod, List<Integer> tagIds, Integer currentRating, LocalDateTime createdAt, Pageable pageable) {
//    OrderSpecifier[] orderSpecifiers = createOrderSpecifier(sortMethod);
//        return queryFactory
//                .selectFrom(review)
//                .where(
//                        eqTags(tagIds), isLowerThenRating(currentRating), isBeforeCreatedAt(createdAt)
//                )
//                .groupBy(review.id)
//                .orderBy(
//                        orderSpecifiers
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//    }


    // 리뷰의 생성시간 <= 주어진 생성시간 이면 true
    private BooleanExpression isBeforeCreatedAt(LocalDateTime createdAt) {
        return createdAt != null ? review.createdAt.loe(createdAt) : null;
    }

    // 리뷰의 별점 <= 주어진 별점값 이면 true
    private BooleanExpression isLowerThenRating(Integer currentRating) {
        return currentRating != null ? review.rating.loe(currentRating) : null;
    }

    // 모든 태그가 포함된 리뷰면 true
    private BooleanExpression eqTags(List<Integer> tagIds){
        return tagIds != null && !tagIds.isEmpty() ? Expressions.allOf(tagIds.stream().map(this::isContainsTagId).toArray(BooleanExpression[]::new)) : null;
    }

    // 한 태그가 포함된 리뷰면 true
    private BooleanExpression isContainsTagId(Integer tagId) {
        switch (tagId) {
            case 1:
                return review.tags.MENU_SELL_BEAN.isTrue();
            case 2:
                return review.tags.MENU_SELL_DESSERT.isTrue();
            case 3:
                return review.tags.MENU_GOOD_COFFEE.isTrue();
            case 4:
                return review.tags.MENU_HAND_DRIP.isTrue();
            case 5:
                return review.tags.MENU_SELF_ROASTING.isTrue();
            case 101:
                return review.tags.ETC_GOOD_WORK.isTrue();
            case 102:
                return review.tags.ETC_GOOD_STUDY.isTrue();
            case 103:
                return review.tags.ETC_GOOD_MOOD.isTrue();
            case 104:
                return review.tags.ETC_GOOD_DATING.isTrue();
            case 105:
                return review.tags.ETC_GOOD_TALKING.isTrue();
            case 106:
                return review.tags.ETC_WITH_PAT.isTrue();
            case 107:
                return review.tags.ETC_WITH_CHILD.isTrue();
            case 108:
                return review.tags.ETC_HAVE_ROOM.isTrue();
            case 109:
                return review.tags.ETC_HAVE_OUTSIDE.isTrue();
            case 110:
                return review.tags.ETC_COMFORTABLE_SEAT.isTrue();
            case 111:
                return review.tags.ETC_CLEAN_TOILET.isTrue();
            case 112:
                return review.tags.ETC_SPACIOUS_STORE.isTrue();
            case 113:
                return review.tags.ETC_STAFF_KIND.isTrue();
            default:
                throw new TagNotFoundException(ErrorCode.TAG_NOT_FOUND_ERROR);
        }

    }

}
