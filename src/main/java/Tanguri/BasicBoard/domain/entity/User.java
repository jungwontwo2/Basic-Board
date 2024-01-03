package Tanguri.BasicBoard.domain.entity;

import Tanguri.BasicBoard.domain.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // DB에서 구분할 아이디

    private String loginId;     //로그인할 때 사용하는 아이디
    private String password;    //비밀번호
    private String nickname;    //닉네임

    private UserRole userRole;  //권한
}
