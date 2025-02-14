package cafeLogProject.cafeLog.domains.user.repository;

import cafeLogProject.cafeLog.api.user.dto.*;
import cafeLogProject.cafeLog.domains.follow.domain.QFollow;
import cafeLogProject.cafeLog.domains.review.domain.QReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static cafeLogProject.cafeLog.domains.follow.domain.QFollow.follow;
import static cafeLogProject.cafeLog.domains.review.domain.QReview.*;
import static cafeLogProject.cafeLog.domains.user.domain.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsNicknameExcludingSelf(String username, String newNickname) {

        return queryFactory
                .selectFrom(user)
                .where(user.nickname.eq(newNickname)
                        .and(user.username.ne(username)))
                .fetchOne() != null;
    }

    @Override
    public List<UserSearchRes> findByNicknameContainingIgnoreCase(String nickname) {

        return queryFactory
                .select(new QUserSearchRes(
                        user.id,
                        user.nickname,
                        user.introduce
                ))
                .from(user)
                .where(user.nickname.startsWith(nickname))
                .fetch();
    }

    @Override
    public Optional<OtherUserInfoRes> findOtherUserInfo(String currentUsername, Long otherUserId) {

        OtherUserInfoRes otherUser = queryFactory
                .select(new QOtherUserInfoRes(
                        user.id,
                        user.nickname,
                        user.introduce,
                        user.email,
                        user.isImageExist,
                        follow.id.isNotNull().as("isFollow"),
                        user.followerCnt,
                        user.followingCnt,
                        review.count().intValue()
                ))
                .from(user)
                .leftJoin(follow)
                .on(follow.follower.username.eq(currentUsername)
                        .and(follow.following.id.eq(otherUserId)))
                .leftJoin(review)
                .on(review.user.eq(user))
                .where(user.id.eq(otherUserId))
                .groupBy(user.id, follow.id)
                .fetchOne();

        return Optional.ofNullable(otherUser);
    }

    @Override
    public Optional<UserInfoRes> findMyProfileWithReviewCount(String username) {

        UserInfoRes reviewCnt = queryFactory
                .select(new QUserInfoRes(
                        user.id,
                        user.nickname,
                        user.introduce,
                        user.email,
                        user.isImageExist,
                        user.followerCnt,
                        user.followingCnt,
                        review.count().intValue()
                ))
                .from(user)
                .leftJoin(review).on(review.user.eq(user))
                .where(user.username.eq(username))
                .groupBy(user.id)
                .fetchOne();

        return Optional.ofNullable(reviewCnt);
    }


}
