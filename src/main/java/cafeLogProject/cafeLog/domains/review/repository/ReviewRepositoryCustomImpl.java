package cafeLogProject.cafeLog.domains.review.repository;

import cafeLogProject.cafeLog.api.review.dto.QShowReviewResponse;
import cafeLogProject.cafeLog.api.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.review.ReviewInvalidSortError;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.user.domain.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static cafeLogProject.cafeLog.domains.image.domain.QReviewImage.reviewImage;
import static cafeLogProject.cafeLog.domains.review.domain.QReview.review;
import static cafeLogProject.cafeLog.domains.review.domain.QTag.tag;
import static cafeLogProject.cafeLog.domains.user.domain.QUser.user;
import static com.querydsl.core.group.GroupBy.*;


@RequiredArgsConstructor
@Slf4j
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    // Tuple 사용하여 dto 형변환하는 경우
    @Override
    public Optional<ShowReviewResponse> findShowReviewResponseById(Long reviewId) {
        List<Tuple> results = queryFactory
                .from(review)
                .select(review, tag.tagId, reviewImage.id, user)
                .leftJoin(reviewImage).on(review.id.eq(reviewImage.review.id))
                .leftJoin(review.user).fetchJoin()
                .leftJoin(tag).on(review.id.eq(tag.review.id))
                .where(review.id.eq(reviewId))
                .fetch();

        return Optional.ofNullable(toShowReviewResponse(results));
    }

    // dto 형변환하는 함수
    private ShowReviewResponse toShowReviewResponse(List<Tuple> results) {
        Map<Long, ShowReviewResponse> reviewMap = new HashMap<>();
        Long reviewId = null;

        for (Tuple tuple : results) {
            Review reviewEntity = tuple.get(review);
            Integer tagId = tuple.get(tag.tagId);
            UUID reviewImageId = tuple.get(reviewImage.id);
            User userEntity = tuple.get(user);
            reviewId = reviewEntity.getId();

            ShowReviewResponse res = reviewMap.computeIfAbsent(reviewEntity.getId(), key -> new ShowReviewResponse(reviewEntity, userEntity));
            if (tagId != null) {
                res.addTagId(tagId);
            }
            if (reviewImageId != null) {
                res.addImageId(reviewImageId);
            }
        }
        return reviewMap.get(reviewId);
    }

    // Tuple 대신 transform 사용하여 dto 형변환하는 경우
//    @Override
//    public ShowReviewResponse findShowReviewResponseById(Long reviewId) {
//        return queryFactory
//                .from(review)
//                .leftJoin(reviewImage).on(review.id.eq(reviewImage.review.id))
//                .leftJoin(review.user).fetchJoin()
//                .leftJoin(tag).on(review.id.eq(tag.review.id))
//                .where(review.id.eq(reviewId))
//                .transform(
//                        groupBy(review.id).as(
//                                new QShowReviewResponse(
//                                        review,
//                                        list(reviewImage.id),    // 카테시안 곱 문제 방지를 위해 set 사용
//                                        list(tag.tagId)
//                                )
//                        )
//                )
//                .get(reviewId);
//    }

    @Override
    public List<ShowReviewResponse> searchByCafeId(Long cafeId, LocalDateTime createdAt, Pageable pageable) {
        List<Long> reviewIds;
        reviewIds = queryFactory
                .select(review.id)
                .from(review)
                .where(
                        review.cafe.id.eq(cafeId),
                        isBeforeCreatedAt(createdAt)
                )
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return queryFactory
                .from(review)
                .leftJoin(reviewImage).on(review.id.eq(reviewImage.review.id))
                .leftJoin(review.user).fetchJoin()
                .leftJoin(tag).on(review.id.eq(tag.review.id))
                .where(review.id.in(reviewIds))
                .orderBy(review.createdAt.desc())
                .transform(
                        groupBy(review.id).list(
                                new QShowReviewResponse(
                                        review,
                                        review.cafe.id,
                                        review.cafe.cafeName,
                                        set(reviewImage.id),    // 이미지 ID를 리스트로 변환
                                        set(tag.tagId)
                                )
                        )
                );
    }

    @Override
    public List<ShowReviewResponse> searchByUser(User user, LocalDateTime createdAt, Pageable pageable){
        List<Long> reviewIds;
        reviewIds = queryFactory
                .select(review.id)
                .from(review)
                .where(
                        review.user.eq(user),
                        isBeforeCreatedAt(createdAt)
                )
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return queryFactory
                .from(review)
                .leftJoin(reviewImage).on(review.id.eq(reviewImage.review.id))
                .leftJoin(review.user).fetchJoin()
                .leftJoin(tag).on(review.id.eq(tag.review.id))
                .where(review.id.in(reviewIds))
                .orderBy(review.createdAt.desc())
                .transform(
                        groupBy(review.id).list(
                                new QShowReviewResponse(
                                        review,
                                        review.cafe.id,
                                        review.cafe.cafeName,
                                        set(reviewImage.id),    // 이미지 ID를 리스트로 변환
                                        set(tag.tagId)
                                )
                        )
                );
    }

    @Override
    public List<ShowReviewResponse> search(String sortMethod, List<Integer> selectedTagIds, Integer currentRating, LocalDateTime createdAt, Pageable pageable) {
        List<Long> reviewIds;
        OrderSpecifier[] orderSpecifiers = createOrderSpecifier(sortMethod);

        // 조건에 부합하는 review 필터링하기
        if (selectedTagIds == null || selectedTagIds.isEmpty()) {
            // 태그 조건 존재하지 않는 경우
            reviewIds = queryFactory
                    .select(review.id)
                    .from(review)
                    .where(
                            isLowerThenRating(currentRating),
                            isBeforeCreatedAt(createdAt)
                    )
                    .orderBy(orderSpecifiers)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        } else {
            // 태그 조건 존재하는 경우
            reviewIds = queryFactory
                    .select(review.id)
                    .from(review)
                    .join(tag).on(tag.review.id.eq(review.id))
                    .where(
                            tag.tagId.in(selectedTagIds),
                            isLowerThenRating(currentRating),
                            isBeforeCreatedAt(createdAt)
                    )
                    .orderBy(orderSpecifiers)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .groupBy(review.id)
                    .having(tag.tagId.count().eq((long) selectedTagIds.size()))
                    .fetch();
        }

        // dto화
        return queryFactory
                .from(review)
                .leftJoin(tag).on(tag.review.id.eq(review.id))
                .leftJoin(reviewImage).on(review.id.eq(reviewImage.review.id))
                .leftJoin(review.user).fetchJoin()
                .where(review.id.in(reviewIds))
                .orderBy(orderSpecifiers)
                .transform(
                        groupBy(review.id)
                                .list(
                                        new QShowReviewResponse(
                                                review,
                                                review.cafe.id,
                                                review.cafe.cafeName,
                                                set(reviewImage.id),    // 이미지 ID를 리스트로 변환
                                                set(tag.tagId)
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

    // 리뷰의 생성시간 <= 주어진 생성시간 이면 true
    private BooleanExpression isBeforeCreatedAt(LocalDateTime createdAt) {
        return createdAt != null ? review.createdAt.loe(createdAt) : null;
    }

    // 리뷰의 별점 <= 주어진 별점값 이면 true
    private BooleanExpression isLowerThenRating(Integer currentRating) {
        return currentRating != null ? review.rating.loe(currentRating) : null;
    }

}
