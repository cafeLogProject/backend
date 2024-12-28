package cafeLogProject.cafeLog.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpiredCheckDTO {

    @JsonProperty(value = "access_token_is_expired")
    public boolean accessTokenIsExpired;

    @JsonProperty(value = "refresh_token_is_expired")
    public boolean refreshTokenIsExpired;
}
