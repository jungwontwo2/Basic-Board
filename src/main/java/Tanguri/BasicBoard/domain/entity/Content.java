package Tanguri.BasicBoard.domain.entity;

import Tanguri.BasicBoard.domain.dto.ContentDto;
import Tanguri.BasicBoard.domain.dto.ContentEditDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    public static ContentDto toDto(Content content){
        ContentDto contentDto = ContentDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .texts(content.getTexts())
                .writer(content.getWriter())
                .build();
        return contentDto;
    }
    public static ContentEditDto toEditDto(Content content){
        ContentEditDto contentEditDto = ContentEditDto.builder()
                .title(content.getTitle())
                .texts(content.getTexts())
                .build();
        return contentEditDto;
    }
}
