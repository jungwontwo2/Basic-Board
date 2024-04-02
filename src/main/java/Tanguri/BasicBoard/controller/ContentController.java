package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.dto.comment.CommentResponseDto;
import Tanguri.BasicBoard.domain.dto.content.ContentDto;
import Tanguri.BasicBoard.domain.dto.content.ContentEditDto;
import Tanguri.BasicBoard.domain.dto.content.ContentPagingDto;
import Tanguri.BasicBoard.domain.dto.content.ContentWriteDto;
import Tanguri.BasicBoard.domain.dto.image.BoardImageUploadDTO;
import Tanguri.BasicBoard.domain.dto.user.CustomUserDetails;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.service.CommentService;
import Tanguri.BasicBoard.service.ContentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;
    private final CommentService commentService;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    private String maxUploadSizeExceeded(HttpServletRequest request){
        request.setAttribute("msg","이미지 크기는 1Mb 이하여야합니다");
        request.setAttribute("redirectUrl","/boards/free/write");
        return "common/messageRedirect";
    }


    @GetMapping("/boards/free")
    public String greetingBoardsSearchword(@PageableDefault(page = 1) Pageable pageable,
                                           @RequestParam(name = "searchWord",required = false)String searchWord,
                                           @RequestParam(value = "orderby",required = false,defaultValue = "id") String orderCriteria,
                                           Model model){
        if(searchWord==null){
            Page<ContentPagingDto> contentDtos = contentService.paging(pageable,orderCriteria);
            int blockLimit = 3;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());



            model.addAttribute("contentDtos", contentDtos);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }
        else {
            //Page<ContentDto> contentDtos = contentService.getBoardListBySearchword(pageable, searchWord);
            Page<ContentDto> contentDtos = contentService.getBoardListBySearchword(pageable, searchWord);
            int blockLimit = 3;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());

            model.addAttribute("contentDtos", contentDtos);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }
        return "content/free";
    }

    //글 보기 화면
    @GetMapping("/boards/free/{id}")
    public String showContent(@PathVariable Long id,
                              @RequestParam(required = false,name = "parentId")Long parentId,
                              Model model,Authentication authentication){

        Content content = contentService.getContent(id);
        ContentDto contentDto = ContentDto.builder().content(content).build();
        List<CommentResponseDto> commentResponseDtos = commentService.commentDtoList(id);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String loggedInNickname = userDetails.getNickname();

        model.addAttribute("loggedInNickname",loggedInNickname);

        model.addAttribute("content",contentDto);
        model.addAttribute("comments",commentResponseDtos);
        return "content/content-page";
    }
    //글쓰기 화면 ㄱㄱ
    @GetMapping("/boards/free/write")
    public String writePage(HttpServletRequest request,@ModelAttribute(name = "content")ContentWriteDto content){
        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "common/messageRedirect";
        }
        return "content/write-page";
    }
    //글 등록
    @PostMapping("/boards/free/write")
    public String writeContent(@ModelAttribute(name = "content") ContentWriteDto content, HttpServletRequest request,
                               @ModelAttribute BoardImageUploadDTO boardImageUploadDTO,
                               Authentication authentication, BindingResult bindingResult) throws IOException {
        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "common/messageRedirect";
        }
        //BindingResult bindingresult = contentService.writeValid(content, bindingResult);
        //System.out.println("bindingresult = " + bindingresult);
        if(content.getTexts().isEmpty() || content.getTitle().isEmpty()){
            request.setAttribute("msg","게시글의 제목이나 내용이 비어있습니다.");
            request.setAttribute("redirectUrl","/boards/free");
            return "common/messageRedirect.html";
        }
        if(content.getTexts().getBytes("UTF-8").length>500){
            request.setAttribute("msg", "게시글 내용이 너무 깁니다. 500바이트를 초과할 수 없습니다.");
            request.setAttribute("redirectUrl", "/boards/free/write");
            return "common/messageRedirect.html";
        }
        if (bindingResult.hasErrors()) {
            return "users/addMemberForm";
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        contentService.writeContent(content,user,boardImageUploadDTO);
        return "redirect:/boards/free";
    }


    //글 수정하기
    //글 수정하기
    @PostMapping("/boards/free/edit/{id}")
    public String editConent(@PathVariable Long id,@ModelAttribute("content")ContentEditDto contentEditDto){
        contentService.editContent(id,contentEditDto);
        return "redirect:/boards/free/"+id;
    }

    //글 수정페이지 가져오기
    @PostMapping("/boards/free/editPage/{id}")
    public String editContent(@PathVariable Long id,@ModelAttribute("content")ContentEditDto contentEditDto,HttpServletRequest request){
        Content content = contentService.getContent(id);
        if(!content.getPassword().equals(contentEditDto.getPassword())){
            request.setAttribute("msg", "비밀번호가 일치하지 않습니다.");
            String redirectUrl = "/boards/free/"+id.toString();
            request.setAttribute("redirectUrl",redirectUrl);
            return "common/messageRedirect";
        }
        ContentEditDto contentEditDto1 = Content.toEditDto(content);
        contentEditDto.setTitle(contentEditDto1.getTitle());
        contentEditDto.setTexts(contentEditDto1.getTexts());
        //contentService.editContent(id, contentEditDto);
        return "content/edit-page";
    }

    //글 삭제
    @PostMapping("/boards/free/delete/{id}")
    public String deleteContent(@PathVariable Long id,@ModelAttribute("content") ContentEditDto contentEditDto,HttpServletRequest request){
        Content content = contentService.getContent(id);
        if(!content.getPassword().equals(contentEditDto.getPassword())){
            request.setAttribute("msg", "비밀번호가 일치하지 않습니다.");
            String redirectUrl = "/boards/free/"+id.toString();
            request.setAttribute("redirectUrl",redirectUrl);
            return "common/messageRedirect";
        }
        contentService.deleteContent(id);
        System.out.println("delete complete");
        return"redirect:/boards/free";
    }
}
