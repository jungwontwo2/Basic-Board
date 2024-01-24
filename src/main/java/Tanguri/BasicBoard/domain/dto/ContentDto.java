package Tanguri.BasicBoard.domain.dto;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.entity.Comment;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentDto {

    private Long id;

    @NotEmpty
    private String title;
    @NotEmpty
    private String texts;

    private String password;
    private String writer;

    private List<Comment> comments;

    public static Content toEntity(ContentDto contentDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER)User user){
        Content content = Content.builder()
                .title(contentDto.title)
                .texts(contentDto.texts)
                .user(user)
                .writer(user.getNickname())
                .password(user.getPassword())
                .build();
        return content;
    }
    public ContentDto(Content content){
        this.password=content.getPassword();
        this.title=content.getTitle();
        this.texts=content.getTexts();
        this.id=content.getId();
        this.writer=content.getWriter();
        this.comments=content.getComments();
    }

}
