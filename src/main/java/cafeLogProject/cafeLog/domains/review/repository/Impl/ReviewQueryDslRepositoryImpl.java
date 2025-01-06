package cafeLogProject.cafeLog.domains.review.repository.Impl;

import cafeLogProject.cafeLog.domains.review.domain.QReview;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.object.SqlQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
public class ReviewQueryDslRepositoryImpl implements ReviewQueryDslRepository{
    private final JPAQueryFactory queryFactory;

    
    // 일부 태그만 포함된 게시글도 리턴되는 오류 존재
    @Override
    public List<Review> findByTagsAndDateTimeOrderByRatingAndDateTime(List<Integer> tagIds, LocalDateTime createdAt, int currentRating) {
        QReview review = QReview.review;

        Expression<String> concatReviewTagIds = Expressions.stringTemplate("CONCAT('[', {0}, ']')", review.tagIds);   //tagIds를 문자열 "[값,값,값]" 형식으로 변환
        Expression<String> concatTagIds = Expressions.stringTemplate("CONCAT('%',{0},'%')", tagIds);   //tagIds를 문자열 "%[값,값,값]%" 형식으로 변환

        return queryFactory
                .selectFrom(review)
                .where(
                    review.tagIds.any().in(tagIds)              // 태그 조건 tagIds가 최소 하나 이상 일치하는 리뷰
                            .and(review.createdAt.lt(createdAt))   // createdAt보다 과거인 리뷰
                            .and(review.rating.loe(currentRating))  // 현재 별점보다 작거나 같은 리뷰
                )
                .groupBy(review.id)  // 리뷰별로 그룹화
                .having(review.tagIds.size().goe(tagIds.size()))  // 모든 tagId가 포함된 리뷰만 반환
                .orderBy(
                        review.rating.desc(),           // 별점 내림차순
                        review.createdAt.desc()         // 생성시간 내림차순
                )
                .fetch();
    }

    @Override
    public List<Review> findByTagsAndDateTimeOrderByDateTime(List<Integer> tagIds, LocalDateTime createdAt) {
        QReview review = QReview.review;
        return queryFactory
                .selectFrom(review)
                .where(
                        review.tagIds.any().in(tagIds) // 태그 조건 tagIds가 최소 하나 이상 일치하는 리뷰
                                .and(review.createdAt.lt(createdAt)) // createdAt보다 과거인 리뷰
                )
                .groupBy(review.id)  // 리뷰별로 그룹화
                .having(review.tagIds.size().goe(tagIds.size()))  // 모든 tagId가 포함된 리뷰만 반환
                .fetch();
    }

    @Override
    public List<Review> findBeforeDateTimeOrderByRatingAndDateTime(LocalDateTime createdAt, int currentRating) {
        QReview review = QReview.review;
        return queryFactory
                .selectFrom(review)
                .where(
                    review.createdAt.lt(createdAt)   // createdAt보다 과거인 리뷰
                        .and(review.rating.loe(currentRating))  // 현재 별점보다 작거나 같은 리뷰
                )
                .groupBy(review.id)  // 리뷰별로 그룹화
                .orderBy(
                        review.rating.desc(),           // 별점 내림차순
                        review.createdAt.desc()         // 생성시간 내림차순
                )
                .fetch();
    }

    @Override
    public List<Review> findBeforeDateTime(LocalDateTime createdAt) {
        QReview review = QReview.review;
        return queryFactory
                .selectFrom(review)
                .where(review.createdAt.lt(createdAt))
                .fetch();
    }

    @Override
    public List<Review> findByTagsContaining(List<Integer> tagIds) {
        QReview review = QReview.review;
        return queryFactory
                .selectFrom(review)
                .where(review.tagIds.any().in(tagIds)) // tagIds가 최소 하나 이상 일치하는 리뷰
                .groupBy(review.id)  // 리뷰별로 그룹화
                .having(review.tagIds.size().goe(tagIds.size()))  // 모든 tagId가 포함된 리뷰만 반환 
                .fetch();
    }


}
