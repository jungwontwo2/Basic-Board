package Tanguri.BasicBoard.domain.dto.content;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.entity.Comment;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
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

    private String title;

    private String texts;

    private String password;
    private String writer;

    private List<Comment> comments;
    private Integer commentCnt;

    private Integer heartCnt;

    public static Content toEntity(ContentDto contentDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER)User user){
        Content content = Content.builder()
                .title(contentDto.getTitle())
                .texts(contentDto.getTexts())
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
        this.writer=content.getUser().getNickname();
        this.comments=content.getComments();
        this.commentCnt= content.getComments().size();
        this.heartCnt=content.getHeartCnt();
    }

}