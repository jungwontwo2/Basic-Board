package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.dto.ContentDto;
import Tanguri.BasicBoard.domain.dto.ContentEditDto;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    public void writeContent(ContentDto contentDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER)User user){
        Content content = ContentDto.toEntity(contentDto,user);
        contentRepository.save(content);
        System.out.println(content);
    }

    public Page<ContentDto> paging(Pageable pageable){
        int page=pageable.getPageNumber()-1;//page위치에 있는 값은 0부터 시작한다.
        int pageLimit = 5;//한페이지에 보여줄 글 개수
        System.out.println("zz");
        Page<Content> contents = contentRepository.findAll(PageRequest.of(page, pageLimit, Sort.Direction.DESC,"id"));
        System.out.println(contents);
        Page<ContentDto> contentsDto = contents.map(content -> new ContentDto(content));
        System.out.println(contentsDto);
        return contentsDto;
    }
    public void editContent(Long id, ContentEditDto contentEditDto){
        Optional<Content> byId = contentRepository.findById(id);
        Content content = byId.get();
        content.setTitle(contentEditDto.getTitle());
        content.setTexts(contentEditDto.getTexts());
        contentRepository.save(content);
    }

    public void deleteContent(Long id, String password) {
        Optional<Content> sample = contentRepository.findById(id);
        if(sample.isEmpty()){
            return;
        }
        Content content = sample.get();
        if (!content.getPassword().equals(password)) {
            return;
        }
        contentRepository.delete(content);
    }

    public List<Content> getAllContents(){
        return contentRepository.findAll();
    }

    public Content getContent(Long id){
        Optional<Content> sample = contentRepository.findById(id);
        return sample.orElseGet(sample::get);
    }
}
