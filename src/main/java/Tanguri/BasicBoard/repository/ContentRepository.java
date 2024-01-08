package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.entity.Content;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ContentRepository {
    private static Map<Long, Content> contents = new HashMap<>();
    private static Long sequence = 0L;

    public void save(Content content){
        content.setId(++sequence);
        contents.put(content.getId(),content);
    }

    public void edit(Long id,Content content){
        contents.put(id,content);
    }

    public void delete(Long id){
        contents.remove(id);
    }

    public List<Content> findAll(){
        return new ArrayList<>(contents.values());
    }

    public Content findById(Long id){
        return contents.get(id);
    }
}
