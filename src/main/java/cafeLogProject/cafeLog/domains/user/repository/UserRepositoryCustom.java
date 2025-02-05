package cafeLogProject.cafeLog.domains.user.repository;

public interface UserRepositoryCustom {

    boolean existsNicknameExcludingSelf(String username, String newNickname);
}
