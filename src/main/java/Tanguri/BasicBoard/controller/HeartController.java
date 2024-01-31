package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.service.HeartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;
    @PostMapping("/heart/{id}")
    public String giveHeart(HttpServletRequest request, @PathVariable Long id, @SessionAttribute(value = SessionConst.LOGIN_MEMBER,required = false) User user,
                            Model model) throws Exception {
        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "/common/messageRedirect";
        }
        System.out.println(user.getLoginId());
        heartService.addHeart(user.getLoginId(),id);
        return "redirect:/boards/free/{id}";
    }

}
