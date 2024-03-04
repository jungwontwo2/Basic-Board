package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.dto.user.EditUserDto;
import Tanguri.BasicBoard.domain.dto.user.JoinUserDto;
import Tanguri.BasicBoard.domain.dto.user.UserNicknameUpdateDto;
import Tanguri.BasicBoard.domain.entity.Image;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.ImageRepository;
import Tanguri.BasicBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
                .url("/profileImages/anonymous.png")
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
}
