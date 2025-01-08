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
//    @Query("SELECT r FROM Review r JOIN r.tags t WHERE t IN :tags GROUP BY r HAVING COUNT(t) = :tagSize")
//        QReview review = QReview.review;
//        return queryFactory
//                .selectFrom(review)
//                .join(review.tagIds, tagId)
//                .where(tagId.in(tagIds))
//                .groupBy(review.id)
//                .fetch();

        return null;


    }


}
