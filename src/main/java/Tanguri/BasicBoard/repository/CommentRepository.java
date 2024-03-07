package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.entity.Comment;
import Tanguri.BasicBoard.domain.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByContent(Content content);

    Page<Comment> findAll(Pageable pageable);
}
