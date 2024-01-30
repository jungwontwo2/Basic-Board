package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.dto.content.ContentDto;
import Tanguri.BasicBoard.domain.dto.content.ContentEditDto;
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

    //글 입력
    public void writeContent(ContentDto contentDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER)User user){
        Content content = ContentDto.toEntity(contentDto,user);
        contentRepository.save(content);
        System.out.println(content);
    }
    //페이징(가입인사)
    public Page<ContentDto> paging(Pageable pageable){
        int page=pageable.getPageNumber()-1;//page위치에 있는 값은 0부터 시작한다.
        int pageLimit = 5;//한페이지에 보여줄 글 개수
        //System.out.println("zz");
        Page<Content> contents = contentRepository.findAll(PageRequest.of(page, pageLimit, Sort.Direction.DESC,"id"));
        Page<ContentDto> contentsDto = contents.map(content -> new ContentDto(content));
        return contentsDto;
    }
    //페이징(user가 쓴 글)
    public Page<ContentDto> pagingByUserId(Pageable pageable,String writer){
        int page=pageable.getPageNumber()-1;//page위치에 있는 값은 0부터 시작한다.
        int pageLimit = 5;//한페이지에 보여줄 글 개수
        Page<Content> contents = contentRepository.findByWriter(PageRequest.of(page, pageLimit, Sort.Direction.DESC,"id"), writer);
        Page<ContentDto> contentDtos = contents.map(content -> new ContentDto(content));
        return contentDtos;
    }
    //게시판 수정
    public void editContent(Long id, ContentEditDto contentEditDto){
        Optional<Content> byId = contentRepository.findById(id);
        Content content = byId.get();
        content.setTitle(contentEditDto.getTitle());
        content.setTexts(contentEditDto.getTexts());
        contentRepository.save(content);
    }
    //게시판 삭제
    public void deleteContent(Long id) {
        Optional<Content> sample = contentRepository.findById(id);
        if(sample.isEmpty()){
            return;
        }
        Content content = sample.get();
        contentRepository.delete(content);
    }
    //모든 게시판 가져오기
    public List<Content> getAllContents(){
        return contentRepository.findAll();
    }
    //Content에 있는 id값으로 Content 가져오기
    public Content getContent(Long id){
        Optional<Content> sample = contentRepository.findById(id);
        return sample.orElseGet(sample::get);
    }

    public Page<Content> getBoardByWriter(Pageable pageable,String writer){
        return contentRepository.findByWriter(pageable,writer);
    }
    public Page<ContentDto> getBoardListBySearchword(Pageable pageable,String searchword){
        int page=pageable.getPageNumber()-1;//page위치에 있는 값은 0부터 시작한다.
        int pageLimit = 5;//한페이지에 보여줄 글 개수
        Page<Content> contents = contentRepository.findByTitleContaining(PageRequest.of(page, pageLimit, Sort.Direction.DESC, "id"), searchword);
        Page<ContentDto> contentDtos = contents.map(content -> new ContentDto(content));
        return contentDtos;
    }
    public List<Content> getBoardListById(Long id){
        List<Content> allById = contentRepository.findAllById(id);
        System.out.println(allById.size());
        return allById;
    }

    public void updateContentWriter(String writer,User user){
        List<Content> contents = user.getContents();
        for (Content content : contents) {

            content.updateWriter(writer);
            contentRepository.save(content);
            System.out.println(content.getWriter());
        }
    }
}
