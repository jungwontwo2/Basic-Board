package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.dto.user.CustomUserDetails;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.service.HeartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class HeartController {
//    @ExceptionHandler(CustomEx.class)
//    public String heartException(HttpServletRequest request){
//        String id = extractIdFromRequestUri(request.getRequestURI());
//        request.setAttribute("msg","본인의 게시물에 좋아요를 누를 수 없습니다");
//        //String redirectUrl = request.getRequestURI();
//        request.setAttribute("redirectUrl","/boards/free/"+id);
//        return "/common/messageRedirect";
//    }

    private String extractIdFromRequestUri(String requestUri) {
        // requestUri에서 id 추출하는 로직을 작성
        // 예를 들어, URL이 /boards/free/20 이면 "20"을 추출하는 방법을 사용
        // 여기서는 단순히 마지막 슬래시 뒤의 문자열을 추출하는 방식으로 예시를 들겠습니다.
        int lastSlashIndex = requestUri.lastIndexOf('/');
        return requestUri.substring(lastSlashIndex + 1);
    }

    private final HeartService heartService;
    @PostMapping("/heart/{id}")
    public String giveHeart(HttpServletRequest request, @PathVariable Long id, Authentication authentication,
                            Model model) throws Exception {
        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "common/messageRedirect";
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        heartService.addHeart(user.getUsername(),id);
        return "redirect:/boards/free/{id}";
    }

}
