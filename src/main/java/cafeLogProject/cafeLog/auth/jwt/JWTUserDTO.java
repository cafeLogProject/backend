package cafeLogProject.cafeLog.auth.jwt;

import cafeLogProject.cafeLog.entity.enums.UserRole;
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
