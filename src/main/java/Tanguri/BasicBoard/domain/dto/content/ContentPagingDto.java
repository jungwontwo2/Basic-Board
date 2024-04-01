package Tanguri.BasicBoard.domain.dto.content;

import Tanguri.BasicBoard.domain.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContentPagingDto {
    private Long id;
    private String title;
    private Integer heartCnt;
    private String writer;
    private boolean isImportant;
    private Integer commentCnt;

    public ContentPagingDto(Content content){
        id=content.getId();
        title=content.getTitle();
        heartCnt=content.getHeartCnt();
        writer=content.getWriter();
        isImportant=content.isImportant();
        commentCnt=content.getCommentCnt();
    }
}
