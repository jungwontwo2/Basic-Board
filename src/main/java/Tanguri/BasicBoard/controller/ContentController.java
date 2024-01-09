package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.dto.ContentDto;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;


    //글쓰기 화면 ㄱㄱ
    @GetMapping("/content/write")
    public String writePage(){
        return "write-page";
    }
    //글 등록
    @PostMapping("/content/write")
    public String writeContent(ContentDto contentDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER)User user) {
        contentService.writeContent(contentDto,user);
        return "redirect:/";
    }

    //글 보기 화면
    @GetMapping("/content/{id}")
    public String showContent(@PathVariable Long id, Model model) {
        model.addAttribute(contentService.getContent(id));
        return "content-page";
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
