package Tanguri.BasicBoard.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordUpdateDto {
    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 4,max = 10,message = "최소 4자 이상, 10자 이하로 입력하세요")
    private String currentPassword;
    @NotBlank(message = "변경할 비밀번호를 입력하세요")
    @Size(min = 4,max = 10,message = "최소 4자 이상, 10자 이하로 입력하세요")
    private String changePassword;
    @NotBlank(message = "변경할 비밀번호를 입력하세요")
    @Size(min = 4,max = 10,message = "최소 4자 이상, 10자 이하로 입력하세요")
    private String checkPassword;
}
