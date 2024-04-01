package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ContentRepository extends JpaRepository<Content, Long> {
    Page<Content> findAll(Pageable pageable);
    Page<Content> findByWriter(Pageable pageable,String writer);
    Page<Content> findByUserId(Pageable pageable,Long id);
    @Query("select c from Content c join fetch c.user u where u.loginId =:loginId")
    Page<Content> findByUserLoginId(Pageable pageable,@Param("loginId") String loginId);
    Page<Content> findByTitleContaining(Pageable pageable,String searchword);

    List<Content> findAllById(Long id);
    //@Query("SELECT c FROM Content c WHERE c.title LIKE %:searchword% ORDER BY c.isImportant DESC, c.boardId DESC")
    @Query(value = "SELECT c FROM Content c WHERE c.title LIKE %:searchword% ORDER BY c.isImportant DESC, c.id DESC")
    Page<Content> findByTitleContainingOrderByIsImportantDescAndContentIdDesc(Pageable pageable, @Param("searchword") String searchwordWithWildcards);

}
