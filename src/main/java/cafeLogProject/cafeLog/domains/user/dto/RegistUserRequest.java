package cafeLogProject.cafeLog.domains.user.dto;

import cafeLogProject.cafeLog.domains.review.domain.enums.UserRole;
import cafeLogProject.cafeLog.domains.user.domain.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RegistUserRequest {
    private String username;

    private String profileImage;

    private String nickname;

    private String introduce;

    private String email;

    private String provider;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;

    @Builder
    public RegistUserRequest (String username, String profileImage, String nickname, String introduce, String email, String provider, UserRole role) {
        this.username = username;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.introduce = introduce;
        this.email = email;
        this.provider = provider;
        this.role = role;
    }

    public User toEntity() {
        LocalDateTime createdAt = LocalDateTime.now();
        return User.builder()
                .username(username)
                .profileImage(profileImage)
                .nickname(nickname)
                .introduce(introduce)
                .email(email)
                .provider(provider)
                .role(role)
                .createdAt(createdAt)
                .build();
    }
}
