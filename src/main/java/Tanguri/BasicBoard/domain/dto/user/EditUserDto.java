package Tanguri.BasicBoard.domain.dto.user;

import Tanguri.BasicBoard.domain.entity.Image;
import Tanguri.BasicBoard.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import javax.swing.plaf.BorderUIResource;

@Data
public class EditUserDto {

    private String loginId;

    private String nickname;

    private Image image;

    @Builder
    public EditUserDto(User user){
        this.loginId=user.getLoginId();
        this.nickname=user.getNickname();
        this.image=user.getImage();
    }
}
