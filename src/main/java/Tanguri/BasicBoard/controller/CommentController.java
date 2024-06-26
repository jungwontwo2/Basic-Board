package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.dto.comment.CommentRequestDto;
import Tanguri.BasicBoard.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
                               Authentication authentication) {
        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "common/messageRedirect";
        }
        String parentId = request.getParameter("parentId");
        commentService.writeComment(commentRequestDto,id,parentId,authentication);
        return "redirect:/boards/free/"+id;
    }
    @PostMapping("/comment/update/{id}")
    public String updateComment(@PathVariable Long id,
                                @RequestParam("text") String updatedComment,
                                @RequestParam("boardId") Long boardId){
        commentService.updateComment(updatedComment,id);
        return "redirect:/boards/free/"+boardId;
    }
}
