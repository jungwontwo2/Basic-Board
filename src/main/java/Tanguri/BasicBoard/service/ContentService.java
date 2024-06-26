package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.dto.content.ContentDto;
import Tanguri.BasicBoard.domain.dto.content.ContentEditDto;
import Tanguri.BasicBoard.domain.dto.content.ContentPagingDto;
import Tanguri.BasicBoard.domain.dto.content.ContentWriteDto;
import Tanguri.BasicBoard.domain.dto.image.BoardImageUploadDTO;
import Tanguri.BasicBoard.domain.dto.user.CustomUserDetails;
import Tanguri.BasicBoard.domain.entity.BoardImage;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.BoardImageRepository;
import Tanguri.BasicBoard.repository.ContentRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    @Value("${file.boardImagePath}")
    private String uploadFolder;

    private final BoardImageRepository boardImageRepository;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public BindingResult writeValid(ContentWriteDto content,BindingResult bindingResult){
        if(content.getTitle().isEmpty()){
            bindingResult.addError(new FieldError("content","title","제목이 비어있습니다"));
        }
        else if(content.getTexts().isEmpty()){
            bindingResult.addError(new FieldError("content","texts","내용이 비어있습니다."));
        }
        return bindingResult;
    }
    //글 입력
    public Long writeContent(ContentWriteDto contentDto, CustomUserDetails user, BoardImageUploadDTO boardImageUploadDTO) throws IOException {
        Content content = ContentWriteDto.toEntity(contentDto, user);
        contentRepository.save(content);//일단 게시물 자체에 대한 내용은 저장.(제목 내용 글쓴이)

        //만약 이미지가 있는 상태로 등록된다면
        if (boardImageUploadDTO.getFiles() != null && !boardImageUploadDTO.getFiles().isEmpty()) {
            for (MultipartFile file : boardImageUploadDTO.getFiles()) {//iter를 돌려서
                if(file.isEmpty()){
                    continue;
                }
                String saveImageUrl = saveBoardImage(file);
                BoardImage image = BoardImage.builder()
                        .url(saveImageUrl)
                        .content(content)
                        .build();
                boardImageRepository.save(image);
            }
        }
        return content.getId();
    }
    public void writeImportantContent(ContentWriteDto contentDto, CustomUserDetails user, BoardImageUploadDTO boardImageUploadDTO) throws IOException {
        Content content = ContentWriteDto.toImportantEntity(contentDto, user);
        contentRepository.save(content);//일단 게시물 자체에 대한 내용은 저장.(제목 내용 글쓴이)

        //만약 이미지가 있는 상태로 등록된다면
        if (boardImageUploadDTO.getFiles() != null && !boardImageUploadDTO.getFiles().isEmpty()) {
            for (MultipartFile file : boardImageUploadDTO.getFiles()) {//iter를 돌려서
                if(file.isEmpty()){
                    continue;
                }
                String saveImageUrl = saveBoardImage(file);
                BoardImage image = BoardImage.builder()
                        .url(saveImageUrl)
                        .content(content)
                        .build();
                boardImageRepository.save(image);
            }
        }
    }
    private String saveBoardImage(MultipartFile multipartFile) throws IOException {
        String filename = getUUIDFileName(multipartFile);
        ObjectMetadata metadata = setObjectMetadata(multipartFile);
        amazonS3.putObject(bucket, filename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, filename).toString();
    }
    private static ObjectMetadata setObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }
    private static String getUUIDFileName(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String filename = uuid+"_"+originalFilename;
        return filename;
    }
    //페이징(가입인사)
    public Page<ContentPagingDto> paging(Pageable pageable,String criteria){
        int page=pageable.getPageNumber()-1;//page위치에 있는 값은 0부터 시작한다.
        int pageLimit = 8;//한페이지에 보여줄 글 개수
        //System.out.println("zz");
        PageRequest pageRequest = PageRequest.of(page, pageLimit, Sort.by(Sort.Order.desc("isImportant"), Sort.Order.desc(criteria),Sort.Order.desc("id")));
        Page<Content> contents = contentRepository.findAll(pageRequest);
        Page<ContentPagingDto> contentsDto = contents.map(content -> new ContentPagingDto(content));
        return contentsDto;
    }
    //페이징(user가 쓴 글)
    public Page<ContentDto> pagingByUserId(Pageable pageable,Long id){
        int page=pageable.getPageNumber()-1;//page위치에 있는 값은 0부터 시작한다.
        int pageLimit = 5;//한페이지에 보여줄 글 개수
        Page<Content> contents = contentRepository.findByUserId(PageRequest.of(page, pageLimit, Sort.Direction.DESC,"id"), id);
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
    public Page<ContentDto> getBoardListBySearchword(Pageable pageable,String searchword){
        int page=pageable.getPageNumber()-1;//page위치에 있는 값은 0부터 시작한다.
        int pageLimit = 8;//한페이지에 보여줄 글 개수
        //Page<Content> contents = contentRepository.findByTitleContaining(PageRequest.of(page, pageLimit, Sort.Direction.DESC, "id"), searchword);
        PageRequest pageRequest = PageRequest.of(page, pageLimit, Sort.by(Sort.Order.desc("isImportant"), Sort.Order.desc("id")));
        //Page<Content> contents = contentRepository.findByTitleContaining(pageRequest, searchword);
        Page<Content> contents = contentRepository.findByTitleContainingOrderByIsImportantDescAndContentIdDesc(pageRequest, searchword);
        Page<ContentDto> contentDtos = contents.map(content -> new ContentDto(content));
        return contentDtos;
    }
    public List<Content> getBoardListById(Long id){
        List<Content> allById = contentRepository.findAllById(id);
        System.out.println(allById.size());
        return allById;
    }

    public Page<ContentPagingDto> pagingByLoginId(Pageable pageable, String loginId) {
        int page=pageable.getPageNumber()-1;//page위치에 있는 값은 0부터 시작한다.
        int pageLimit = 5;//한페이지에 보여줄 글 개수
        Page<Content> contents = contentRepository.findByUserLoginId(PageRequest.of(page, pageLimit, Sort.Direction.DESC,"id"), loginId);
        Page<ContentPagingDto> contentDtos = contents.map(content -> new ContentPagingDto(content));
        return contentDtos;
    }


}
