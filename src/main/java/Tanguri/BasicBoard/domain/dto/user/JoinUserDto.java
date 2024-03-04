package Tanguri.BasicBoard.domain.dto.user;

import Tanguri.BasicBoard.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class JoinUserDto {

    @NotBlank(message = "아이디를 입력하세요")
    @Size(min = 4,max = 10,message = "최소 4자 이상, 10자 이하로 입력하세요")
    @Pattern(regexp = "^[a-z0-9]*$", message = "알파벳 소문자(a~z), 숫자(0~9)만 입력 가능합니다.")
    private String loginId;
    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 8,max = 20,message = "최소 8자 이상, 20자 이하로 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "알파벳 대소문자(a~z, A~Z), 숫자(0~9)만 입력 가능합니다.")
    private String password;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 8,max = 20,message = "최소 8자 이상, 20자 이하로 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "알파벳 대소문자(a~z, A~Z), 숫자(0~9)만 입력 가능합니다.")
    private String passwordCheck;

    @NotBlank(message = "닉네임을 입력하세요")
    @Size(min = 2,max = 10,message = "최소 2자 이상, 10자 이하로 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "닉네임은 숫자, 한글, 영어만 가능합니다.")
    private String nickname;



    public static User toEntity(String loginId,String password,String nickname) {
        User user = new User(loginId,password,nickname,"ROLE_USER");
        System.out.println("userDto->Entity success");
        // 기타 필요한 필드 복사 작업 수행
        return user;
    }
}
