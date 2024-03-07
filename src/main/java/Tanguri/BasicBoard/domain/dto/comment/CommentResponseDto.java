package Tanguri.BasicBoard.domain.dto.comment;

import Tanguri.BasicBoard.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String imageUrl;
    private List<CommentResponseDto> children;
    private boolean hasChildren;
    private boolean hasParent;


    //CommentEntity를 넣었을 때 Dto로 바꿔주는 역할
    public CommentResponseDto(Comment comment) {
        System.out.println(comment.getCreatedAt());
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.nickname = comment.getUser().getNickname();
        this.imageUrl=comment.getUser().getImage().getUrl();
        this.children=new ArrayList<>();
        this.hasChildren=!comment.getChildren().isEmpty();
        this.hasParent= comment.getParent() != null;//comment.getParent가 null이 아니면 parent가 있다는 뜻(true반환)
    }
}
