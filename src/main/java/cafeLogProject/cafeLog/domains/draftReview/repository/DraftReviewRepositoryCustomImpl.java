package cafeLogProject.cafeLog.domains.draftReview.repository;

import cafeLogProject.cafeLog.api.draftReview.dto.QShowDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.dto.QShowUserDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowUserDraftReviewResponse;
import cafeLogProject.cafeLog.domains.draftReview.domain.DraftReview;
import cafeLogProject.cafeLog.domains.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static cafeLogProject.cafeLog.domains.draftReview.domain.QDraftReview.draftReview;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.dsl.Expressions.set;

@RequiredArgsConstructor
@Slf4j
public class DraftReviewRepositoryCustomImpl implements DraftReviewRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    // 항상 res를 null값으로 리턴하는 오류, 원인 모름
    @Override
    public Optional<ShowDraftReviewResponse> findShowDraftReviewResponseById(Long draftReviewId) {
        ShowDraftReviewResponse res = queryFactory
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
                                            draftReview.cafe.id
                                    )
                            )
                    ).get(draftReviewId);
        return Optional.ofNullable(res);
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
