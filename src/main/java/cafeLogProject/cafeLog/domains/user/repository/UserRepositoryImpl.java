package cafeLogProject.cafeLog.domains.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

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
}
