package cafeLogProject.cafeLog.jwt;

import cafeLogProject.cafeLog.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTUserDTO {

    private Long userId;
    private String username;
    private UserRole userRole;
}
