package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.dto.JoinUserDto;
import Tanguri.BasicBoard.domain.dto.LoginUserDto;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.UserRepository;
import Tanguri.BasicBoard.service.LoginService;
import Tanguri.BasicBoard.service.UserService;
import Tanguri.BasicBoard.session.SessionManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final LoginService loginService;
    private final UserService userService;
    private final SessionManager sessionManager;

    //회원가입 누름
    @GetMapping("/users/join")
    public String addUserForm(@ModelAttribute("user") JoinUserDto user) {
        return "users/addMemberForm";
    }

    //회원가입 폼 제출
    @PostMapping("/users/join")
    public String addUser(@Validated @ModelAttribute("user") JoinUserDto user, BindingResult bindingResult) {
        if(!user.getPassword().equals(user.getPasswordCheck())){
            bindingResult.rejectValue("passwordCheck","passwordCheckValid","비밀번호가 일치하지 않습니다.");
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
    public String loginForm(@ModelAttribute("user") LoginUserDto user){
        return "/users/login";
    }
    @PostMapping("users/login")
    public String login(@Validated @ModelAttribute("user") LoginUserDto user, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "users/login";
        }
        User loginUser = loginService.login(user.getLoginId(), user.getPassword());
        if(loginUser==null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "users/login";
        }

        sessionManager.createSession(loginUser,response);

        return "redirect:/home-login";
    }
    @GetMapping("/home-login")
    public String homeLogin(HttpServletRequest request, Model model){
        User user = (User) sessionManager.getSession(request);
        if(user==null){
            return "home/home";
        }
        model.addAttribute("User",user);
        return "home/home-login";
    }
}
