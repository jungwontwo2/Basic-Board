package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.dto.ContentDto;
import Tanguri.BasicBoard.domain.entity.Content;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Transactional
public class ContentRepository {
    private final EntityManager em;

    public void save(Content content){
        em.persist(content);
    }

    public void edit(Long id,Content content){
        Content contentById = findById(id);
        contentById.setTitle(content.getTitle());
        contentById.setTexts(content.getTexts());
    }

    public void delete(Long id){
        em.remove(id);
    }

    public List<Content> findAll(){
        return em.createQuery("select m from Content m",Content.class).getResultList();
    }

    public Content findById(Long id){
        return em.find(Content.class,id);
    }
}
