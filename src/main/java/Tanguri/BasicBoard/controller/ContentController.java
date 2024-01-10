package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.dto.ContentDto;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
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

@Controller
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/boards/greeting")
    public String greetingsBoards(HttpServletRequest request, @PageableDefault(page = 1) Pageable pageable, Model model){
//        HttpSession session = request.getSession(false);
//        if(session==null){
//            request.setAttribute("msg","로그인 후 사용가능합니다.");
//            request.setAttribute("redirectUrl","/users/login");
//            return "/common/messageRedirect";
//        }
//        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);
//        if (loginUser == null) {
//            request.setAttribute("msg","로그인 후 사용가능합니다.");
//            request.setAttribute("redirectUrl","/users/login");
//            return "/common/messageRedirect";
//        }
        Page<ContentDto> contentDtos = contentService.paging(pageable);
        /**
         * blockLimit : page 개수 설정
         * 현재 사용자가 선택한 페이지 앞 뒤로 3페이지씩만 보여준다.
         * ex : 현재 사용자가 4페이지라면 2, 3, (4), 5, 6
         */
        int blockLimit = 3;
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());

        model.addAttribute("contentDtos", contentDtos);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/content/greeting";
    }
    //글쓰기 화면 ㄱㄱ
    @GetMapping("/boards/greeting/write")
    public String writePage(@ModelAttribute(name = "content")ContentDto contentDto){
        return "/content/write-page";
    }
    //글 등록
    @PostMapping("/boards/greeting/write")
    public String writeContent(ContentDto contentDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER)User user) {
        contentService.writeContent(contentDto,user);
        return "redirect:/boards/greeting";
    }

    //글 보기 화면
    @GetMapping("/boards/greeting/{id}")
    public String showContent(@PathVariable Long id, Model model) {
        Content content = contentService.getContent(id);
        ContentDto contentDto = Content.toDto(content);
        model.addAttribute("content",contentDto);
        return "/content/content-page";
    }

    //글 수정
    @PostMapping("/content/{id}")
    public String editContent(@PathVariable Long id, Content content) {
//        contentService.editContent(id, content.getTexts(), content.getPassword());
        return "redirect:/";
    }

    //글 삭제
    @PostMapping("/content/delete/{id}")
    public String deleteContent(@PathVariable Long id,Content content){
        contentService.deleteContent(id, content.getPassword());
        return"redirect:/";
    }
}
