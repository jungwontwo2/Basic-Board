package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    //로그인 체크
    //성공시 User 엔티티 반환,실패시 null 반환
    public User login(String userId,String password){
        return userRepository.findByLoginId(userId).filter(m->m.getPassword().equals(password)).orElse(null);
    }
}
