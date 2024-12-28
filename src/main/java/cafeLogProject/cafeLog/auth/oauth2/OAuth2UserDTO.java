package cafeLogProject.cafeLog.auth.oauth2;

import cafeLogProject.cafeLog.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static cafeLogProject.cafeLog.entity.enums.UserRole.ROLE_USER;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2UserDTO {

    private String username;
    private String email;
    private String provider;
    private UserRole role;

}
