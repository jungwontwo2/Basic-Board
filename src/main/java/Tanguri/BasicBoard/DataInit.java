package Tanguri.BasicBoard;

import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.ContentRepository;
import Tanguri.BasicBoard.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    //테스트용 데이터 추가
    @PostConstruct
    public void dataInit(){
        User user = new User("asdf","asdfasdf","asdf");
        userRepository.save(user);
        System.out.println("Init User Complete");

        User user2 = userRepository.findByNickname("asdf");
        Content content = Content.builder()
                .title("1")
                .texts("1")
                .user(user)
                .writer(user.getNickname())
                .password(user.getPassword())
                .build();
        contentRepository.save(content);
        System.out.println("Init content Complete");
    }


}
