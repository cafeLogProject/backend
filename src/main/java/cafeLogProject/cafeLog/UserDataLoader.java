package cafeLogProject.cafeLog;

import cafeLogProject.cafeLog.api.follow.service.FollowService;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserDataLoader {

    private final UserRepository userRepository;
    private final FollowService followService;

    @PostConstruct
    @Transactional
    public void fakeUser() {

        userRepository.save(createUser("google_11111", "커피", "user1@google.com", "google", false));
        userRepository.save(createUser("google_22222", "커피와 나", "user2@google.com", "google", false));
        userRepository.save(createUser("google_33333", "맛난 커피", "user3@google.com", "google", false));
        userRepository.save(createUser("google_44444", "커피애호가", "user4@google.com", "google", false));
        userRepository.save(createUser("google_55555", "커피한잔", "user5@google.com", "google", false));
        userRepository.save(createUser("google_66666", "아이스커피", "user6@google.com", "google", false));
        userRepository.save(createUser("google_77777", "커피중독", "user7@google.com", "google", false));
        userRepository.save(createUser("google_88888", "커피사랑", "user8@google.com", "google", false));
        userRepository.save(createUser("google_99999", "응커피", "user9@google.com", "google", false));
        userRepository.save(createUser("naver_11111", "코피가아닌커피", "user1@naver.com", "naver", false));
        userRepository.save(createUser("naver_22222", "커피토끼", "user2@naver.com", "naver", false));
        userRepository.save(createUser("naver_33333", "사자가커피를사자", "user3@naver.com", "naver", false));
        userRepository.save(createUser("naver_44444", "커피하마", "user4@naver.com", "naver", false));
        userRepository.save(createUser("naver_55555", "길을잃은커피", "user5@naver.com", "naver", false));
        userRepository.save(createUser("naver_66666", "커피사장", "user6@naver.com", "naver", false));
        userRepository.save(createUser("naver_77777", "커피사상가", "user7@naver.com", "naver", false));
        userRepository.save(createUser("naver_88888", "커피마시는임금", "user8@naver.com", "naver", false));
        userRepository.save(createUser("naver_99999", "커피123", "user9@naver.com", "naver", false));

        Long google11111 = userRepository.findByUsername("google_11111").get().getId();
        Long google22222 = userRepository.findByUsername("google_22222").get().getId();
        Long google33333 = userRepository.findByUsername("google_33333").get().getId();
        Long google44444 = userRepository.findByUsername("google_44444").get().getId();
        Long google55555 = userRepository.findByUsername("google_55555").get().getId();
        Long google66666 = userRepository.findByUsername("google_66666").get().getId();
        Long google77777 = userRepository.findByUsername("google_77777").get().getId();
        Long google88888 = userRepository.findByUsername("google_88888").get().getId();
        Long google99999 = userRepository.findByUsername("google_99999").get().getId();

        Long naver11111 = userRepository.findByUsername("naver_11111").get().getId();
        Long naver22222 = userRepository.findByUsername("naver_22222").get().getId();
        Long naver33333 = userRepository.findByUsername("naver_33333").get().getId();
        Long naver44444 = userRepository.findByUsername("naver_44444").get().getId();
        Long naver55555 = userRepository.findByUsername("naver_55555").get().getId();
        Long naver66666 = userRepository.findByUsername("naver_66666").get().getId();
        Long naver77777 = userRepository.findByUsername("naver_77777").get().getId();
        Long naver88888 = userRepository.findByUsername("naver_88888").get().getId();
        Long naver99999 = userRepository.findByUsername("naver_99999").get().getId();


        followService.followUser("google_11111", google22222);
        followService.followUser("google_33333", google22222);
        followService.followUser("google_44444", google22222);
        followService.followUser("google_55555", google22222);
        followService.followUser("google_66666", google22222);
        followService.followUser("google_77777", google22222);

        followService.followUser("google_11111", google22222);
        followService.followUser("google_11111", naver11111);
        followService.followUser("google_22222", google33333);
        followService.followUser("google_22222", naver22222);
        followService.followUser("google_33333", google44444);
        followService.followUser("google_33333", naver33333);
        followService.followUser("google_44444", google55555);
        followService.followUser("google_55555", google66666);
        followService.followUser("google_66666", google77777);
        followService.followUser("google_77777", naver44444);
        followService.followUser("google_88888", naver55555);
        followService.followUser("google_99999", naver66666);

        followService.followUser("google_11111", google22222);
        followService.followUser("google_22222", google33333);
        followService.followUser("google_33333", google44444);
        followService.followUser("google_44444", google55555);
        followService.followUser("google_55555", google66666);
        followService.followUser("google_66666", google77777);
        followService.followUser("google_77777", google88888);
        followService.followUser("google_88888", google99999);
        followService.followUser("google_99999", google11111);
        followService.followUser("naver_11111", google11111);
        followService.followUser("naver_22222", google22222);
        followService.followUser("naver_33333", google33333);
        followService.followUser("naver_44444", google44444);
        followService.followUser("naver_55555", google55555);
        followService.followUser("naver_66666", google66666);
        followService.followUser("naver_77777", google77777);
        followService.followUser("naver_88888", google88888);
        followService.followUser("naver_99999", google99999);

        followService.followUser("naver_11111", naver22222);
        followService.followUser("naver_22222", naver33333);
        followService.followUser("naver_33333", naver44444);
        followService.followUser("naver_44444", naver55555);
        followService.followUser("naver_55555", naver66666);
        followService.followUser("naver_66666", naver77777);
        followService.followUser("naver_77777", naver88888);
        followService.followUser("naver_88888", naver99999);

        followService.followUser("google_11111", naver33333);
        followService.followUser("google_22222", naver44444);
        followService.followUser("naver_11111", google44444);
        followService.followUser("naver_22222", google55555);
        followService.followUser("google_33333", naver66666);
        followService.followUser("google_44444", naver77777);
        followService.followUser("naver_33333", google66666);
        followService.followUser("naver_44444", google77777);
        followService.followUser("google_55555", naver88888);
        followService.followUser("naver_55555", google88888);
        followService.followUser("google_66666", naver99999);
        followService.followUser("naver_66666", google99999);

    }

    private User createUser(String username, String nickname, String email, String provider, boolean isImageExist) {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .email(email)
                .provider(provider)
                .isImageExist(isImageExist)
                .build();
    }
}
