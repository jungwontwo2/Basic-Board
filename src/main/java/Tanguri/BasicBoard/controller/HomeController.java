package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ContentService contentService;
    //초기화면
    @GetMapping(value = {"", "/"})
    public String home() {
        //model.addAttribute("contents", contentService.getAllContents());
        return "home/home";
    }

}
