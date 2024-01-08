package Tanguri.BasicBoard;

import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final UserRepository userRepository;

    //테스트용 데이터 추가
    @PostConstruct
    public void init(){
        User user = new User("asdf","asdfasdf","asdf");
        userRepository.save(user);
        System.out.println("Init User Complete");
    }
}
