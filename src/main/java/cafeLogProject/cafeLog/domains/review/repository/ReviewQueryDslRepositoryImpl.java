package cafeLogProject.cafeLog.domains.review.repository;

import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.domains.review.dto.ShowReviewResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
public class ReviewQueryDslRepositoryImpl implements ReviewQueryDslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> findReviewsByBeforeCreatedAt(LocalDateTime localDateTime) {
//        QReview review = QReview.review;
////    @Query("SELECT r FROM Review r WHERE r.createdAt < :dateTime")
//        return queryFactory
//                .select(review)

        return null;
    }
}
