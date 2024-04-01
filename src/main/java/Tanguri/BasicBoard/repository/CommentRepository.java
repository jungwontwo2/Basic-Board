package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.entity.Comment;
import Tanguri.BasicBoard.domain.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByContent(Content content);

    @Query("select c from Comment c join fetch c.user u join fetch u.image where c.content = :content")
    List<Comment> findByContentWithUserImage(@Param("content") Content content);

    Page<Comment> findAll(Pageable pageable);
}
