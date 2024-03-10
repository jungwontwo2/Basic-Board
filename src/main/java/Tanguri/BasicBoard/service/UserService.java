package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.dto.comment.CommentResponseDto;
import Tanguri.BasicBoard.domain.dto.user.AdminResponseDto;
import Tanguri.BasicBoard.domain.dto.user.EditUserDto;
import Tanguri.BasicBoard.domain.dto.user.JoinUserDto;
import Tanguri.BasicBoard.domain.dto.user.UserNicknameUpdateDto;
import Tanguri.BasicBoard.domain.entity.Comment;
import Tanguri.BasicBoard.domain.entity.Image;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.ImageRepository;
import Tanguri.BasicBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void saveUser(JoinUserDto userDto){
        User user = JoinUserDto.toEntity(userDto.getLoginId(),bCryptPasswordEncoder.encode(userDto.getPassword()),userDto.getNickname());
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
        //userRepository.save(updateUser);
        return updateUser;
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
    }
}
