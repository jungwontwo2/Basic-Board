package Tanguri.BasicBoard.domain.dto.content;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.dto.user.CustomUserDetails;
import Tanguri.BasicBoard.domain.entity.BoardImage;
import Tanguri.BasicBoard.domain.entity.Comment;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto {

    private Long id;

    private String title;

    private String texts;

    private String password;
    private String writer;

    private List<Comment> comments;
    private Integer commentCnt;

    private Integer heartCnt;

    private List<String> imageUrls;

    private String loginId;
    private boolean isImportant;

    @Builder
    public ContentDto(Content content){
        this.password=content.getPassword();
        this.title=content.getTitle();
        this.texts=content.getTexts();
        this.id=content.getId();
        this.writer=content.getUser().getNickname();
        this.comments=content.getComments();
        this.commentCnt= content.getComments().size();
        this.heartCnt=content.getHeartCnt();
        this.imageUrls=content.getBoardImages().stream().map(BoardImage::getUrl).collect(Collectors.toList());
        this.isImportant=content.isImportant();
    }

}
