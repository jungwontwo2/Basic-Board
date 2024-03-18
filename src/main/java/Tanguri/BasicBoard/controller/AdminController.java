package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.dto.comment.CommentResponseDto;
import Tanguri.BasicBoard.domain.dto.content.ContentDto;
import Tanguri.BasicBoard.domain.dto.content.ContentWriteDto;
import Tanguri.BasicBoard.domain.dto.image.BoardImageUploadDTO;
import Tanguri.BasicBoard.domain.dto.user.AdminResponseDto;
import Tanguri.BasicBoard.domain.dto.user.CustomUserDetails;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.service.CommentService;
import Tanguri.BasicBoard.service.ContentService;
import Tanguri.BasicBoard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final ContentService contentService;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/admin")
    public String admin(@RequestParam(name = "search",required = false)String search,
                        @PageableDefault(page = 1) Pageable pageable,
                        @RequestParam(value = "orderby",required = false,defaultValue = "id") String orderCriteria,
                        Model model){
        if(search==null||search.equals("contents")){
            String criteria = "contents";
            Page<ContentDto> contentDtos = contentService.paging(pageable,orderCriteria);
            int blockLimit = 3;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());

            model.addAttribute("dtos", contentDtos);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("criteria",criteria);
        } else if (search.equals("comments")) {
            String criteria = "comments";
            Page<CommentResponseDto> commentsDtos = commentService.paging(pageable);
            int blockLimit = 3;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), commentsDtos.getTotalPages());

            model.addAttribute("dtos", commentsDtos);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("criteria",criteria);
        } else if (search.equals("users")) {
            String criteria = "users";
            Page<AdminResponseDto> adminResponseDtos = userService.paging(pageable);
            int blockLimit = 3;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), adminResponseDtos.getTotalPages());

            model.addAttribute("dtos", adminResponseDtos);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("criteria",criteria);
            System.out.println("users");
        }

        return "admin/adminEdit";
    }
    @PostMapping("/admin/content/{id}/delete")
    public String adminDeleteContent(@PathVariable Long id){
        contentService.deleteContent(id);
        return "redirect:/admin";
    }
    @PostMapping("/admin/comment/{id}/delete")
    public String adminDeleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return "redirect:/admin?search=comments";
    }

    @PostMapping("/admin/user/{id}/delete")
    public String adminDeleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return "redirect:/admin?search=users";
    }
    @PostMapping("admin/user/{id}/change/ROLE_ADMIN")
    public String userUpdateRoleAdmin(@PathVariable Long id){
        userService.changeRole(id,"ROLE_ADMIN");
        return "redirect:/admin?search=users";
    }
    @PostMapping("admin/user/{id}/change/ROLE_USER")
    public String userUpdateRoleUser(@PathVariable Long id){
        userService.changeRole(id,"ROLE_USER");
        return "redirect:/admin?search=users";
    }
    @GetMapping("admin/board/write")
    public String adminGetWriteImportantBoard(@ModelAttribute("content")ContentWriteDto content){
        return "admin/admin-content";
    }

    @PostMapping("admin/board/write")
    public String adminWriteImportantBoard(@ModelAttribute("content")ContentWriteDto content, Authentication authentication,
                                           @ModelAttribute BoardImageUploadDTO boardImageUploadDTO) throws IOException {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        contentService.writeImportantContent(content,user,boardImageUploadDTO);
        return "redirect:/boards/free";
    }
}
