package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ContentRepository extends JpaRepository<Content, Long> {
    Page<Content> findAll(Pageable pageable);
    Page<Content> findByWriter(Pageable pageable,String writer);
    Page<Content> findByUserId(Pageable pageable,Long id);
    Page<Content> findByUserLoginId(Pageable pageable,String loginId);
    Page<Content> findByTitleContaining(Pageable pageable,String searchword);

    List<Content> findAllById(Long id);
}
