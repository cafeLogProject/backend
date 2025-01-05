package cafeLogProject.cafeLog.domains.review.repository;

import cafeLogProject.cafeLog.domains.review.domain.QReview;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
public class ReviewQueryDslRepositoryImpl implements ReviewQueryDslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> findByTagIdsContainingAndBeforeCreatedAt(List<Integer> tagIds, LocalDateTime createdAt) {
        QReview review = QReview.review;
        return queryFactory
                .selectFrom(review)
                .where(
                    review.tagIds.any().in(tagIds) // 태그 조건 tagIds가 최소 하나 이상 일치하는 리뷰
                            .and(review.createdAt.lt(createdAt)) // 생성일 조건
                )
                .groupBy(review.id)  // 리뷰별로 그룹화
                .having(review.tagIds.size().goe(tagIds.size()))  // 모든 tagId가 포함된 리뷰만 반환
                .fetch();
    }

    @Override
    public List<Review> findReviewsByBeforeCreatedAt(LocalDateTime createdAt) {
//    @Query("SELECT r FROM Review r WHERE r.createdAt < :dateTime")
        QReview review = QReview.review;
        return queryFactory
                .selectFrom(review)
                .where(review.createdAt.lt(createdAt))
                .fetch();
    }

    @Override
    public List<Review> findByTagIdsContaining(List<Integer> tagIds) {
        QReview review = QReview.review;
        return queryFactory
                .selectFrom(review)
                .where(review.tagIds.any().in(tagIds)) // tagIds가 최소 하나 이상 일치하는 리뷰
                .groupBy(review.id)  // 리뷰별로 그룹화
                .having(review.tagIds.size().goe(tagIds.size()))  // 모든 tagId가 포함된 리뷰만 반환 
                .fetch();
    }


}
