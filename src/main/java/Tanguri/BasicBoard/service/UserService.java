package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.dto.comment.CommentResponseDto;
import Tanguri.BasicBoard.domain.dto.user.AdminResponseDto;
import Tanguri.BasicBoard.domain.dto.user.EditUserDto;
import Tanguri.BasicBoard.domain.dto.user.JoinUserDto;
import Tanguri.BasicBoard.domain.dto.user.UserNicknameUpdateDto;
import Tanguri.BasicBoard.domain.entity.Comment;
import Tanguri.BasicBoard.domain.entity.Image;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.error.CustomException;
import Tanguri.BasicBoard.error.ErrorCode;
import Tanguri.BasicBoard.repository.ImageRepository;
import Tanguri.BasicBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SessionRegistry sessionRegistry;

    @Transactional
    public void saveUser(JoinUserDto userDto,String role){
        if(userRepository.existsByLoginId(userDto.getLoginId())){
            throw new CustomException(ErrorCode.HAS_LOGIN_ID);
        } else if (userRepository.existsByNickname(userDto.getNickname())) {
            throw new CustomException(ErrorCode.HAS_NICKNAME);
        }
        User user = JoinUserDto.toEntity(userDto.getLoginId(),bCryptPasswordEncoder.encode(userDto.getPassword()),userDto.getNickname(),role);
        Image image = Image.builder()
                .url("https://basicboard-images.s3.ap-northeast-2.amazonaws.com/anonymous.png")
                .user(user)
                .build();
        userRepository.save(user);
        imageRepository.save(image);
    }
    @Transactional
    public User updateUserNickname(UserNicknameUpdateDto userDto){
        Optional<User> optionalUser = userRepository.findByLoginId(userDto.getLoginId());
        User updateUser = optionalUser.get();
        updateUser.updateNickname(userDto.getNickname());
        userRepository.save(updateUser);
        updateAuthenticationInSession(userDto.getNickname());
        return updateUser;
    }

    private void updateAuthenticationInSession(String nickname) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) currentAuth.getPrincipal();
        //System.out.println("principal.getUsername() = " + principal.getUsername());
        if (principal.getUsername().equals(nickname)) {
            UserDetails userDetails = userRepository.findByNickname(nickname);
            Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
    }

    public EditUserDto findMember(String loginId){
        Optional<User> byLoginId = userRepository.findByLoginId(loginId);
        User user = byLoginId.get();
        //System.out.println(user.getNickname());
        //System.out.println(user.getLoginId());
        EditUserDto result = EditUserDto.builder()
                .user(user)
                .build();
        return result;
    }
    @Transactional(readOnly = true)
    public boolean checkNicknameDuplication(String nickname){
        return userRepository.existsByNickname(nickname);
    }
    public Page<AdminResponseDto> paging(Pageable pageable) {
        int page=pageable.getPageNumber()-1;//page위치에 있는 값은 0부터 시작한다.
        int pageLimit = 8;//한페이지에 보여줄 글 개수
        //System.out.println("zz");
        Page<User> users = userRepository.findAll(PageRequest.of(page, pageLimit, Sort.Direction.DESC, "id"));
        Page<AdminResponseDto> adminResponseDtos = users.map(user -> new AdminResponseDto(user));
        return adminResponseDtos;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        sessionRegistry.getAllPrincipals().stream()
                .filter(user -> user instanceof UserDetails)
                .filter(user -> ((UserDetails) user).getUsername().equals(id))
                .forEach(user -> sessionRegistry.getAllSessions(user, false)
                        .forEach(sessionInformation -> sessionInformation.expireNow()));
    }

    @Transactional
    public void changePassword(String loginId,String changePassword){
        User user = userRepository.findByLoginId(loginId).get();
        user.updatePassword(bCryptPasswordEncoder.encode(changePassword));
        userRepository.save(user);
        updateAuthenticationInSession(user.getNickname());
    }

    public boolean checkPassword(String loginId,String currentPassword){
        Optional<User> user = userRepository.findByLoginId(loginId);
        return (bCryptPasswordEncoder.matches(currentPassword,user.get().getPassword()));
    }

    @Transactional
    public void changeRole(Long Id,String changeRole){
        Optional<User> optionalUser = userRepository.findById(Id);
        User user = optionalUser.get();
        user.updateRole(changeRole);
        userRepository.save(user);
        updateAuthenticationInSession(user.getNickname());
        logoutUser(user.getNickname());
    }

    @Transactional
    public void logoutUser(String username) {
        // 사용자 이름으로 UserDetails를 찾습니다.
        UserDetails userDetails = userRepository.findByNickname(username);
        // 사용자의 모든 세션을 무효화합니다.
        sessionRegistry.getAllSessions(userDetails, false)
                .forEach(sessionInformation -> sessionInformation.expireNow());
    }

    public User getUser(String loginId) {
        return userRepository.findByLoginId(loginId).orElse(null);
    }
}
