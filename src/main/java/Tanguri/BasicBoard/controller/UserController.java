package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.dto.ResponseDto;
import Tanguri.BasicBoard.domain.dto.content.ContentPagingDto;
import Tanguri.BasicBoard.domain.dto.image.ImageResponseDto;
import Tanguri.BasicBoard.domain.dto.user.*;
import Tanguri.BasicBoard.domain.entity.Image;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final LoginService loginService;
    private final UserService userService;

    private final ContentService contentService;
    private final ImageService imageService;

    //private final AuthenticationManager authenticationManager;

    private final S3UploadService s3UploadService;


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
            return "users/addMemberForm";
        }
        userService.saveUser(user,"ROLE_USER");
        return "redirect:/";
    }

    @RequestMapping(value = "/join/loginIdCheck")
    public @ResponseBody ResponseDto<?> check(@RequestBody(required = false) String loginId)  {
        if(loginId==null || loginId.isEmpty()){
            return new ResponseDto<>(-1,"아이디를 입력해주세요.",null);
        }
        if(!loginId.matches("^[a-z0-9]+$") || loginId.length()<4 || loginId.length()>10) {
            return new ResponseDto<>(-1, "아이디는 알파벳 소문자와 숫자만 포함할 수 있습니다. 4글자 이상 10글자 이하로 입력해주세요.", null);
        }
        User user = userService.getUserByLoginId(loginId);
        if(user==null){
            return new ResponseDto<>(1,"사용 가능한 ID입니다.",true);
        }
        else {
            return new ResponseDto<>(1,"중복된 아이디입니다.",false);
        }
    }
    @RequestMapping(value = "/join/nickNameCheck")
    public @ResponseBody ResponseDto<?> checkNickname(@RequestBody(required = false) String nickName)  {
        if(nickName==null || nickName.isEmpty()){
            return new ResponseDto<>(-1,"닉네임을 입력해주세요.",null);
        }
        if(!nickName.matches("^[a-zA-Z0-9가-힣]+$") || nickName.length()<2 || nickName.length()>10) {
            return new ResponseDto<>(-1, "닉네임은 알파벳 대,소문자와 한글과 숫자만 포함할 수 있습니다. 2글자 이상 10글자 이하로 입력해주세요.", null);
        }
        User user = userService.getUserByNickName(nickName);
        if(user==null){
            return new ResponseDto<>(1,"사용 가능한 닉네임입니다.",true);
        }
        else {
            return new ResponseDto<>(1,"중복된 닉네임입니다.",false);
        }
    }
    @GetMapping("users/login")
    public String loginForm(@RequestParam(value = "error",required = false)String error,
                            @RequestParam(value = "exception",required = false)String exception,
                            Model model,@ModelAttribute("user") LoginUserDto user) {
        if(error!=null){
            model.addAttribute("error", error);
            model.addAttribute("exception", exception);
        }
        return "users/login";
    }

    @PostMapping("users/login")
    public String login(@Validated @ModelAttribute("user") LoginUserDto user,
                        BindingResult bindingResult, HttpServletRequest request,
                        Model model) {
        if (bindingResult.hasErrors()) {
            return "users/login";
        }
        User loginUser = loginService.login(user.getLoginId(), user.getPassword());
        if (loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "users/login";
        }
        //세션이 있으면 세션 반환, 없으면 신규 세션 생성
        //HttpSession session = request.getSession();
        //쿠키 하나 생성 key:LOGIN_MEMBER(loginMember) , value:JESSIONID=12309ASDHFFKJH13290F9E UUID값
        //그 쿠키값인 JESSIONID를 key값으로 가지고 value값으로 loginUser를 가지도록 세션스토어에 저장한다
        //session.setAttribute(SessionConst.LOGIN_MEMBER, loginUser);
        //System.out.println("loginsuccess");
        return "redirect:/";
    }

//    @GetMapping("/")
//    public String loginSpring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false) User loginUser,Model model){
//        if(loginUser==null){
//
//            return "home/home";
//        }
//
//        model.addAttribute("user",loginUser);
//        return "home/home-login";
//    }
    @GetMapping("/")
    public String loginSpring(){
      return "home/home";
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
                         Authentication authentication,
                         Model model){
        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "common/messageRedirect";
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        EditUserDto member = userService.findMember(user.getUsername());
        System.out.println("member.getNickname() = " + member.getNickname());
        String loginId = authentication.getName();
        Page<ContentPagingDto> contentDtos = contentService.pagingByLoginId(pageable, loginId);
        //System.out.println(user.getImageUrl());
        //ImageResponseDto image = imageService.findImage(loginId);
        Image image = member.getImage();
        model.addAttribute("image",image);
        int blockLimit = 3;
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());
        model.addAttribute("user",user);
        model.addAttribute("nickname",member.getNickname());
        model.addAttribute("contentDtos", contentDtos);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "users/user-info";
    }
    @GetMapping("/users/my/edit/image")
    public String editImagePage(HttpServletRequest request,
                                Authentication authentication,Model model)
    {
        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "common/messageRedirect";
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        ImageResponseDto image = imageService.findImage(user.getUsername());

        model.addAttribute("user",user);
        model.addAttribute("image",image);
        return "users/image-edit";
    }
    @GetMapping("/users/my/edit/info")
    public String getEditUserInfo(HttpServletRequest request,
                                Authentication authentication,Model model)
    {

        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "common/messageRedirect";
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        EditUserDto userDto = userService.findMember(user.getUsername());
        Image image = userDto.getImage();
        model.addAttribute("image",image);
        model.addAttribute("user", userDto);
        return "users/user-info-edit";
    }
    @PostMapping("/users/my/edit/info")
    public String postEditUserInfo(HttpServletRequest request,
                                   @Validated @ModelAttribute("user") UserNicknameUpdateDto userNicknameUpdateDto,
                                   BindingResult bindingResult, Model model,Authentication authentication)
    {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "common/messageRedirect";
        }
        boolean checkNicknameDuplication = userService.checkNicknameDuplication(userNicknameUpdateDto.getNickname());
        if(checkNicknameDuplication){
            bindingResult.rejectValue("nickname","nicknameDuplicate","존재하는 닉네임입니다");
        }
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            ImageResponseDto image = imageService.findImage(userNicknameUpdateDto.getLoginId());
            model.addAttribute("image",image);
            return "users/user-info-edit";
        }
        userService.updateUserNickname(userNicknameUpdateDto);
        return "redirect:/users/my";
    }

    @GetMapping("/users/update/password")
    public String getUpdatePassword(@ModelAttribute("user") PasswordUpdateDto user){
        return "users/password-edit";
    }

    @PostMapping("users/update/password")
    public String updatePassword(@Valid @ModelAttribute("user") PasswordUpdateDto user, BindingResult bindingResult,
                                 Authentication authentication, HttpServletRequest request){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String loginId = userDetails.getUsername();
        if(!userService.checkPassword(loginId,user.getCurrentPassword())){
            bindingResult.rejectValue("currentPassword","UnCorrectPassword","현재 비밀번호를 제대로 작성해주세요");
        } else if (!Objects.equals(user.getChangePassword(), user.getCheckPassword())) {
            bindingResult.rejectValue("checkPassword","NotSamePassword","변경할 비밀번호를 같게 입력해주세요");
        }
        if(bindingResult.hasErrors()){
            return "users/password-edit";
        }
        else {
            userService.changePassword(loginId,user.getChangePassword());
            request.setAttribute("msg","비밀번호가 변경되었습니다");
            request.setAttribute("redirectUrl","/users/my");
            return "common/messageRedirect";
        }
    }
}
