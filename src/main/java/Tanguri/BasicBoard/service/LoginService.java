package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //로그인 체크
    //성공시 User 엔티티 반환,실패시 null 반환
    public User login(String userId,String password){
        Optional<User> user = userRepository.findByLoginId(userId);
        if(bCryptPasswordEncoder.matches(password,user.get().getPassword())){
            return user.get();
        }
        else return null;
    }
}
