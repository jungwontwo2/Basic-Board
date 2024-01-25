package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.dto.user.JoinUserDto;
import Tanguri.BasicBoard.domain.entity.Image;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.ImageRepository;
import Tanguri.BasicBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public void saveUser(JoinUserDto user){
        User joinuser = JoinUserDto.toEntity(user);
        Image image = Image.builder()
                .url("/profileImages/anonymous.png")
                .user(joinuser)
                .build();
        userRepository.save(joinuser);
        imageRepository.save(image);
    }
}
