package Tanguri.BasicBoard.domain.entity;

import Tanguri.BasicBoard.domain.dto.content.ContentDto;
import Tanguri.BasicBoard.domain.dto.content.ContentEditDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
public class Content extends BaseEntity{

    @Id
    @Column(name = "content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String texts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String writer;
    private String password;

    @OneToMany(mappedBy = "content", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OrderBy("id asc")
    private List<Comment> comments;

    @Column(columnDefinition = "integer default 0")
    private Integer heartCnt;

    @OneToMany(mappedBy = "content",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<BoardImage> boardImages;

//    public static ContentDto toDto(Content content){
//        ContentDto contentDto = ContentDto.builder()
//                .id(content.getId())
//                .title(content.getTitle())
//                .texts(content.getTexts())
//                .writer(content.getUser().getNickname())
//                .heartCnt(content.getHeartCnt())
//                .build();
//        return contentDto;
//    }
    public static ContentEditDto toEditDto(Content content){
        ContentEditDto contentEditDto = ContentEditDto.builder()
                .title(content.getTitle())
                .texts(content.getTexts())
                .build();
        return contentEditDto;
    }
    public void updateWriter(String writer){
        this.writer=writer;
    }
    public void heartChange(Integer heartCnt){
        this.heartCnt=heartCnt;
    }
}
