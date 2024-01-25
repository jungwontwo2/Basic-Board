package Tanguri.BasicBoard.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;            // DB에서 구분할 아이디

    private String loginId;     //로그인할 때 사용하는 아이디
    private String password;    //비밀번호
    private String nickname;    //닉네임

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)    //사용자가 쓴 게시물
    private List<Content> contents;

    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY)
    private Image image;

    public User(String loginId, String password, String nickname) {
        this.loginId=loginId;
        this.password = password;
        this.nickname = nickname;
    }
}
