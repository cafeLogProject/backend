package cafeLogProject.cafeLog.test.repository;

import cafeLogProject.cafeLog.test.domain.QReviewEntity;
import cafeLogProject.cafeLog.test.domain.ReviewEntity;
import cafeLogProject.cafeLog.test.dto.ReviewFindRes;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cafeLogProject.cafeLog.test.domain.QReviewEntity.reviewEntity;
import static cafeLogProject.cafeLog.test.domain.QTagEntity.tagEntity;

@RequiredArgsConstructor
public class ReviewEntityRepositoryImpl implements ReviewEntityRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReviewFindRes> findAllReview() {

        List<Tuple> results = queryFactory
                .select(reviewEntity, tagEntity.tagId)
                .from(reviewEntity)
                .leftJoin(tagEntity).on(tagEntity.reviewEntity.id.eq(reviewEntity.id))
                .fetch();

        Map<Long, ReviewFindRes> reviewMap = new HashMap<>();

        for (Tuple tuple : results) {
            ReviewEntity review = tuple.get(reviewEntity);
            Integer tagId = tuple.get(tagEntity.tagId);

            ReviewFindRes reviewFindRes = reviewMap.computeIfAbsent(review.getId(), id -> new ReviewFindRes(
                    review.getCafe().getId(),
                    review.getUser().getId(),
                    review.getId(),
                    review.getContent(),
                    review.getRating(),
                    review.getVisitDate(),
                    review.getCafe().getAddress(),
                    new ArrayList<>()
            ));

            if (tagId != null) {
                reviewFindRes.getTagIds().add(tagId);
            }
        }

        return new ArrayList<>(reviewMap.values());
    }

    @Override
    public List<ReviewFindRes> findAllByTagIds(List<Integer> selectedTagIds) {

        if (selectedTagIds == null || selectedTagIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> reviewIds = queryFactory
                .select(reviewEntity.id)
                .from(reviewEntity)
                .join(tagEntity).on(tagEntity.reviewEntity.id.eq(reviewEntity.id))
                .where(tagEntity.tagId.in(selectedTagIds))
                .fetch();

        List<Tuple> results = queryFactory
                .select(reviewEntity, tagEntity.tagId)
                .from(reviewEntity)
                .leftJoin(tagEntity).on(tagEntity.reviewEntity.id.eq(reviewEntity.id))
                .where(reviewEntity.id.in(reviewIds))
                .fetch();

        Map<Long, ReviewFindRes> reviewMap = new HashMap<>();

        for (Tuple tuple : results) {
            ReviewEntity review = tuple.get(reviewEntity);
            Integer tagId = tuple.get(tagEntity.tagId);

            ReviewFindRes reviewFindRes = reviewMap.computeIfAbsent(review.getId(), id -> new ReviewFindRes(
                    review.getCafe().getId(),
                    review.getUser().getId(),
                    review.getId(),
                    review.getContent(),
                    review.getRating(),
                    review.getVisitDate(),
                    review.getCafe().getAddress(),
                    new ArrayList<>()
            ));

            if (tagId != null) {
                reviewFindRes.getTagIds().add(tagId);
            }
        }

        return new ArrayList<>(reviewMap.values());
    }

}
