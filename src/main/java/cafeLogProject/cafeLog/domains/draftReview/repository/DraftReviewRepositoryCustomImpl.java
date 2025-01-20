package cafeLogProject.cafeLog.domains.draftReview.repository;

import cafeLogProject.cafeLog.api.draftReview.dto.QShowDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.dto.QShowUserDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowUserDraftReviewResponse;
import cafeLogProject.cafeLog.domains.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static cafeLogProject.cafeLog.domains.draftReview.domain.QDraftReview.draftReview;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.dsl.Expressions.set;

@RequiredArgsConstructor
@Slf4j
public class DraftReviewRepositoryCustomImpl implements DraftReviewRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ShowDraftReviewResponse> findShowDraftReviewResponseById(Long draftReviewId) {
        try{
            ShowDraftReviewResponse res =  queryFactory
                    .from(draftReview)
                    .where(draftReview.id.eq(draftReviewId))
                    .transform(
                            groupBy(draftReview.id).as(
                                    new QShowDraftReviewResponse(
                                            draftReview.id,
                                            draftReview.content,
                                            draftReview.rating,
                                            draftReview.visitDate,
                                            draftReview.imageIds,
                                            draftReview.tagIds,
                                            draftReview.cafe.id,
                                            draftReview.user.id
                                    )
                            )
                    )
                    .get(draftReviewId);
            log.error("res:"+res);
        return Optional.ofNullable(res);
        } catch (Exception e) {
            log.error("에러" + e);
        }
        return Optional.ofNullable(null);
    }


    @Override
    public List<ShowUserDraftReviewResponse> findAllIdsByUser(User user) {
        return queryFactory
                .from(draftReview)
                .where(draftReview.user.eq(user))
                .orderBy(draftReview.modifiedAt.desc())
                .transform(
                        groupBy(draftReview.id).list(
                                new QShowUserDraftReviewResponse(
                                        draftReview.id,
                                        draftReview.modifiedAt,
                                        draftReview.cafe.cafeName
                                )
                        )
                );
    }

}
