package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.dto.CommentRequestDto;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * id=게시물
     * commentRequestDTO 댓글 정보(String으로 댓글내용 담김)
     * SessionAttribute 로그인했는지 확인하기 위함
     */
    @PostMapping("/boards/free/{id}/comment")
    public String writeComment(HttpServletRequest request, @PathVariable Long id, CommentRequestDto commentRequestDto,
                               @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) User user) {
        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "/common/messageRedirect";
        }
        commentService.writeComment(commentRequestDto,id,user.getNickname());
        return "redirect:/boards/free/"+id;
    }
}
