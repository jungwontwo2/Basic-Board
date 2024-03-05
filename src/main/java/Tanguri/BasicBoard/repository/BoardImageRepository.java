package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage,Long> {
}
