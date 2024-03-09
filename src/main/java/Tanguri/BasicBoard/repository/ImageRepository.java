package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.entity.Image;
import Tanguri.BasicBoard.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {

    Image findByUser(User user);
}
