package Tanguri.BasicBoard.domain.entity;

import Tanguri.BasicBoard.domain.dto.ContentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Content {

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

    public static ContentDto toDto(Content content){
        ContentDto contentDto = ContentDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .texts(content.getTexts())
                .build();
        return contentDto;
    }
}
