package cafeLogProject.cafeLog.domains.user.repository;

import cafeLogProject.cafeLog.api.user.dto.QUserSearchRes;
import cafeLogProject.cafeLog.api.user.dto.UserSearchRes;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
}
