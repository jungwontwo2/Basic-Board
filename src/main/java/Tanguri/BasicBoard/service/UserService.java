package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.dto.JoinUserDto;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Join;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void saveUser(JoinUserDto user){
        User joinuser = JoinUserDto.toEntity(user);
        userRepository.save(joinuser);
    }
}
