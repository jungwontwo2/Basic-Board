package Tanguri.BasicBoard.domain.dto.user;

import Tanguri.BasicBoard.domain.entity.Comment;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class AdminResponseDto {
    private String nickname;

    private String loginId;

    private Long Id;

    private String userRole;

    public AdminResponseDto(User user){
        this.nickname=user.getNickname();
        this.loginId=user.getLoginId();
        this.Id=user.getId();
        this.userRole=user.getRole();
    }
}
