package Tanguri.BasicBoard.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNicknameUpdateDto {

    @NotBlank(message = "로그인 ID를 입력해주세요.")
    private String loginId;
    @NotBlank(message = "닉네임을 입력하세요")
    @Size(min=2,max=10,message = "최소 2자 이상, 10자 이하로 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "닉네임은 숫자, 한글, 영어만 가능합니다.")
    private String nickname;
}
