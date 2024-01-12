package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository2 extends JpaRepository<User,Long> {
    User findByNickname(String nickname);
    Optional<User> findByLoginId(String loginId);
}
