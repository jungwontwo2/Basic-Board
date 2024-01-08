package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.dto.JoinUserDto;
import Tanguri.BasicBoard.domain.dto.LoginUserDto;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.UserRepository;
import jakarta.servlet.http.HttpServlet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

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
        User userEntity = JoinUserDto.toEntity(user);
        userRepository.save(userEntity);
        return "redirect:/";
    }

    //로그인 누름
    @GetMapping("users/login")
    public String loginForm(@ModelAttribute("user") LoginUserDto user){
        return "/users/login";
    }
    @PostMapping("users/login")
    public String login(@Validated @ModelAttribute("user") LoginUserDto user,BindingResult bindingResult,HttpServlet httpServlet){
        return "";
    }
    @GetMapping("/home-login")
    public String homeLogin(){
        return "home-login";
    }
}
