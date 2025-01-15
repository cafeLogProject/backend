package cafeLogProject.cafeLog.api.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateReq {

    @JsonProperty(value = "nickname")
    @Size(min = 1, max = 20, message = "닉네임은 1자 이상 20자 이하로 설정해야 합니다.")
    private String nickName;

    @Size(max = 20, message = "자기소개는 20자 이하로 설정해야 합니다.")
    private String introduce;
}
