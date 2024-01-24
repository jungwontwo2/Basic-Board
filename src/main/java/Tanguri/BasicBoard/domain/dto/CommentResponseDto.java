package Tanguri.BasicBoard.domain.dto;

import Tanguri.BasicBoard.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long id;
    private String comment;
    private String nickname;


    //CommentEntity를 넣었을 때 Dto로 바꿔주는 역할
    public CommentResponseDto(Comment comment) {
        System.out.println(comment.getCreatedAt());
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.nickname = comment.getUser().getNickname();
    }
}
