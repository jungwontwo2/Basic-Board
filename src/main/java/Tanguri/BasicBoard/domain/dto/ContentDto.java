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

    private Long id;

    @NotEmpty
    private String title;
    @NotEmpty
    private String texts;

    private String password;
    private String writer;

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
    public ContentDto() {

    }
    public ContentDto(Content content){
        this.password=content.getPassword();
        this.title=content.getTitle();
        this.texts=content.getTexts();
        this.id=content.getId();
        this.writer=content.getWriter();
    }

}
