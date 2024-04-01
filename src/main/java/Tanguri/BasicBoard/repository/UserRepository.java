package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.dto.user.CustomUserDetails;
import Tanguri.BasicBoard.domain.entity.Image;
import Tanguri.BasicBoard.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    //Optional<User> findByNickname(String nickname);
    @Query("select u from User u join fetch u.image where u.loginId=:loginId")
    Optional<User> findByLoginId(@Param("loginId") String loginId);
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);

    @Query("select u from User u join fetch u.image where u.loginId=:loginId")
    User findImageByLoginId(@Param("loginId") String loginId);

    UserDetails findByNickname(String nickname);
    Optional<User> findByPassword(String password);
}
