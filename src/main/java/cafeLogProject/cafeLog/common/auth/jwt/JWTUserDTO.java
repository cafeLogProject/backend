package cafeLogProject.cafeLog.common.auth.jwt;

import cafeLogProject.cafeLog.domains.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTUserDTO {

    private Long userId;
    private String username;
    private UserRole userRole;
}
