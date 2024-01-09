package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.dto.ContentDto;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    public void writeContent(ContentDto contentDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER)User user){
        Content content = ContentDto.toEntity(contentDto,user);
        contentRepository.save(content);
    }

    public void editContent(Long id,ContentDto contentDto){
        Content content = contentRepository.findById(id);
        if (!content.getPassword().equals(contentDto.getPassword())) {
            return;
        }
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
