package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.Heart;
import Tanguri.BasicBoard.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart,Long> {

    boolean existsByUserLoginIdAndContentId(String loginId,Long ContentId);
}
