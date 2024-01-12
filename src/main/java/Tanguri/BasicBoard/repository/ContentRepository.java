package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.dto.ContentDto;
import Tanguri.BasicBoard.domain.entity.Content;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public interface ContentRepository extends JpaRepository<Content, Long> {
//    Page<Content> findAll(Pageable pageable);
    Page<Content> findByWriter(Pageable pageable,String writer);
    Page<Content> findByTitleContaining(Pageable pageable,String searchword);
}
