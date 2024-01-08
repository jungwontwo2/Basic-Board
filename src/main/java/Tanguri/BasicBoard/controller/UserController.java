package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.dto.UserDto;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.UserRepository;
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
    public String addUserForm(@ModelAttribute("user") UserDto user) {
        return "users/addMemberForm";
    }

    //회원가입 폼 제출
    @PostMapping("/users/join")
    public String addUser(@Validated @ModelAttribute("user") UserDto user, BindingResult bindingResult) {
        System.out.println(bindingResult);
        if (bindingResult.hasErrors()) {
            return "/users/addMemberForm";
        }
        User userEntity = UserDto.toEntity(user);
        userRepository.save(userEntity);
        return "redirect:/home-login";
    }

    @GetMapping("/home-login")
    public String homeLogin(){
        return "home-login";
    }
}
