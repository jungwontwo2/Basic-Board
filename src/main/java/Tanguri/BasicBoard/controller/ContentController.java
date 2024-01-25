package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.dto.comment.CommentResponseDto;
import Tanguri.BasicBoard.domain.dto.content.ContentDto;
import Tanguri.BasicBoard.domain.dto.content.ContentEditDto;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.service.CommentService;
import Tanguri.BasicBoard.service.ContentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;
    private final CommentService commentService;

//    @GetMapping("/boards/greeting")
//    public String greetingsBoards(@PageableDefault(page = 1) Pageable pageable, Model model){
////    public String greetingsBoards(HttpServletRequest request, @PageableDefault(page = 1) Pageable pageable, Model model){
////        HttpSession session = request.getSession(false);
////        if(session==null){
////            request.setAttribute("msg","로그인 후 사용가능합니다.");
////            request.setAttribute("redirectUrl","/users/login");
////            return "/common/messageRedirect";
////        }
////        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);
////        if (loginUser == null) {
////            request.setAttribute("msg","로그인 후 사용가능합니다.");
////            request.setAttribute("redirectUrl","/users/login");
////            return "/common/messageRedirect";
////        }
//        Page<ContentDto> contentDtos = contentService.paging(pageable);
//        /**
//         * blockLimit : page 개수 설정
//         * 현재 사용자가 선택한 페이지 앞 뒤로 3페이지씩만 보여준다.
//         * ex : 현재 사용자가 4페이지라면 2, 3, (4), 5, 6
//         */
//        int blockLimit = 3;
//        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
//        int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());
//
//        model.addAttribute("contentDtos", contentDtos);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//        return "/content/greeting";
//    }
    @GetMapping("/boards/free")
    public String greetingBoardsSearchword(@PageableDefault(page = 1) Pageable pageable,
                                           @RequestParam(name = "searchWord",required = false)String searchWord, Model model){
        if(searchWord==null){
            Page<ContentDto> contentDtos = contentService.paging(pageable);
            int blockLimit = 3;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());

            model.addAttribute("contentDtos", contentDtos);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }
        else {
            Page<ContentDto> contentDtos = contentService.getBoardListBySearchword(pageable, searchWord);
            int blockLimit = 3;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());

            model.addAttribute("contentDtos", contentDtos);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }
        return "/content/free";
    }

    //글 보기 화면
    @GetMapping("/boards/free/{id}")
    public String showContent(@PathVariable Long id, Model model){
        Content content = contentService.getContent(id);
        ContentDto contentDto = Content.toDto(content);
        List<CommentResponseDto> commentResponseDtos = commentService.commentDtoList(id);
        for (CommentResponseDto commentResponseDto : commentResponseDtos) {
            System.out.println(commentResponseDto.getCreatedAt());
        }
        model.addAttribute("content",contentDto);
        model.addAttribute("comments",commentResponseDtos);
        return "/content/content-page";
    }
    //글쓰기 화면 ㄱㄱ
    @GetMapping("/boards/free/write")
    public String writePage(HttpServletRequest request,@ModelAttribute(name = "content")ContentDto contentDto,@SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false)User user){
        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "/common/messageRedirect";
        }
        return "/content/write-page";
    }
    //글 등록
    @PostMapping("/boards/free/write")
    public String writeContent(HttpServletRequest request,ContentDto contentDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false)User user) {
        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "/common/messageRedirect";
        }
        contentService.writeContent(contentDto,user);
        return "redirect:/boards/free";
    }

    //글 수정
//    @GetMapping("/boards/greeting/edit/{id}")
//    public String getEditPage(@PathVariable Long id,@RequestParam String password, HttpServletRequest request,Model model) {
//        Content content = contentService.getContent(id);
//        if(!content.getPassword().equals(password)){
//            request.setAttribute("msg", "비밀번호가 일치하지 않습니다.");
//            String redirectUrl = "/boards/greeting/"+id.toString();
//            request.setAttribute("redirectUrl",redirectUrl);
//            return "/common/messageRedirect";
//        }
//        ContentEditDto contentEditDto = Content.toEditDto(content);
//        model.addAttribute("content",contentEditDto);
////        ContentEditDto contentEditDto = new ContentEditDto();
////        contentEditDto.setTitle(contentDto.getTitle());
////        contentEditDto.setTexts(contentDto.getTexts());
////        model.addAttribute("editContentDto",contentEditDto);
//        //contentService.editContent(id, content.getTexts(), content.getPassword());
//        return "/content/edit-page";
//    }
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
            return "/common/messageRedirect";
        }
        ContentEditDto contentEditDto1 = Content.toEditDto(content);
        contentEditDto.setTitle(contentEditDto1.getTitle());
        contentEditDto.setTexts(contentEditDto1.getTexts());
        //contentService.editContent(id, contentEditDto);
        return "/content/edit-page";
    }

    //글 삭제
    @PostMapping("/boards/free/delete/{id}")
    public String deleteContent(@PathVariable Long id,@ModelAttribute("content") ContentEditDto contentEditDto,HttpServletRequest request){
        Content content = contentService.getContent(id);
        if(!content.getPassword().equals(contentEditDto.getPassword())){
            request.setAttribute("msg", "비밀번호가 일치하지 않습니다.");
            String redirectUrl = "/boards/free/"+id.toString();
            request.setAttribute("redirectUrl",redirectUrl);
            return "/common/messageRedirect";
        }
        contentService.deleteContent(id);
        System.out.println("delete complete");
        return"redirect:/boards/free";
    }
}
