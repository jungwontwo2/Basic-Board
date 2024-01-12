package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.dto.ContentDto;
import Tanguri.BasicBoard.domain.dto.JoinUserDto;
import Tanguri.BasicBoard.domain.dto.LoginUserDto;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.UserRepository;
import Tanguri.BasicBoard.service.ContentService;
import Tanguri.BasicBoard.service.LoginService;
import Tanguri.BasicBoard.service.UserService;
import Tanguri.BasicBoard.session.SessionManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final LoginService loginService;
    private final UserService userService;

    private final ContentService contentService;

    //회원가입 누름
    @GetMapping("/users/join")
    public String addUserForm(@ModelAttribute("user") JoinUserDto user) {
        return "users/addMemberForm";
    }

    //회원가입 폼 제출
    @PostMapping("/users/join")
    public String addUser(@Validated @ModelAttribute("user") JoinUserDto user, BindingResult bindingResult) {
        if (!user.getPassword().equals(user.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "passwordCheckValid", "비밀번호가 일치하지 않습니다.");
        }
        System.out.println(bindingResult);
        if (bindingResult.hasErrors()) {
            return "/users/addMemberForm";
        }
        userService.saveUser(user);
        return "redirect:/";
    }

    //로그인 누름
    @GetMapping("users/login")
    public String loginForm(@ModelAttribute("user") LoginUserDto user) {
        return "/users/login";
    }

    @PostMapping("users/login")
    public String login(@Validated @ModelAttribute("user") LoginUserDto user, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "users/login";
        }
        User loginUser = loginService.login(user.getLoginId(), user.getPassword());
        if (loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "users/login";
        }
        //세션이 있으면 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //쿠키 하나 생성 key:LOGIN_MEMBER(loginMember) , value:JESSIONID=12309ASDHFFKJH13290F9E UUID값
        //그 쿠키값인 JESSIONID를 key값으로 가지고 value값으로 loginUser를 가지도록 세션스토어에 저장한다
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginUser);
        System.out.println("로그인 성공");
        return "redirect:/";
    }

    //@GetMapping("/")
    public String homeLogin(HttpServletRequest request, Model model) {
        //세션이 없으면 home
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home/home";
        }
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginUser == null) {
            return "home/home";
        }
        model.addAttribute("user", loginUser);
        //System.out.println(loginUser);
        return "home/home-login";
    }

    @GetMapping("/")
    public String loginSpring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false) User loginUser,Model model){
        if(loginUser==null){
            return "home/home";
        }
        model.addAttribute("user",loginUser);
        return "home/home-login";
    }

    @GetMapping("/users/logout")
    public String logout(HttpServletRequest request) {
        //세션 삭제. getSession(false)이므로 세션을 생성하지 않는다
        HttpSession session = request.getSession(false);
        if(session!=null){
            session.invalidate();
            System.out.println("session deleted");
        }
        return "redirect:/";
    }

    @GetMapping("users/my")
    public String myInfo(@PageableDefault(page = 1) Pageable pageable,HttpServletRequest request,
                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false)User user,
                         Model model){
        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "/common/messageRedirect";
        }
        Page<ContentDto> contentDtos = contentService.pagingByUserId(pageable, user.getNickname());
        int blockLimit = 3;
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());

        model.addAttribute("contentDtos", contentDtos);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/users/user-info";
    }
}
