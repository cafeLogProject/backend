package cafeLogProject.cafeLog.common.auth.oauth2;

import cafeLogProject.cafeLog.domains.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2UserDTO {

    private String username;
    private String email;
    private String provider;
    private UserRole role;

}
