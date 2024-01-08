package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    public void writeContent(Content content){
        contentRepository.save(content);
    }

    public void editContent(Long id,String texts,String password){
        Content content = contentRepository.findById(id);
        if (!content.getPassword().equals(password)) {
            return;
        }
        content.setTexts(texts);
        contentRepository.edit(id,content);
    }

    public void deleteContent(Long id, String password) {
        Content content = contentRepository.findById(id);
        if (!content.getPassword().equals(password)) {
            return;
        }
        contentRepository.delete(id);
    }

    public List<Content> getAllContents(){
        return contentRepository.findAll();
    }

    public Content getContent(Long id){
        return contentRepository.findById(id);
    }
}
