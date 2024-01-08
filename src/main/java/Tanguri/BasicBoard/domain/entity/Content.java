package Tanguri.BasicBoard.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
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
}
