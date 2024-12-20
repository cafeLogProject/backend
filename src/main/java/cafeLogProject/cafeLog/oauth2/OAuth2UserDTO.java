package cafeLogProject.cafeLog.oauth2;

import cafeLogProject.cafeLog.entity.enums.UserRole;
import lombok.Data;

import static cafeLogProject.cafeLog.entity.enums.UserRole.ROLE_USER;

@Data
public class OAuth2UserDTO {

    private String username;
    private String email;
    private String provider;
    private UserRole role = ROLE_USER;

    public OAuth2UserDTO(String username, String email, String provider) {
        this.username = username;
        this.email = email;
        this.provider = provider;
    }
}
