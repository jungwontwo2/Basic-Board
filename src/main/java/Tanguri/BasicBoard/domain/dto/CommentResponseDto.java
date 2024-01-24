package Tanguri.BasicBoard.domain.dto;

import Tanguri.BasicBoard.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long id;
    private String comment;
    private String username;


    //CommentEntity를 넣었을 때 Dto로 바꿔주는 역할
    @Builder
    public CommentResponseDto(Comment comment) {
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.username = comment.getUser().getNickname();
    }
}
