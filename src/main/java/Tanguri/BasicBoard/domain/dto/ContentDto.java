package Tanguri.BasicBoard.domain.dto;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.bind.annotation.SessionAttribute;

@Data
public class ContentDto {

    @NotEmpty
    private String title;
    @NotEmpty
    private String texts;

    private String password;

    public static Content toEntity(ContentDto contentDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER)User user){
        Content content = Content.builder()
                .title(contentDto.title)
                .texts(contentDto.texts)
                .user(user)
                .writer(user.getNickname())
                .password(user.getPassword())
                .build();
        System.out.println(content);
        return content;
    }
}
