package Tanguri.BasicBoard;

import Tanguri.BasicBoard.domain.dto.user.JoinUserDto;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.ContentRepository;
import Tanguri.BasicBoard.repository.UserRepository;
import Tanguri.BasicBoard.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //테스트용 데이터 추가
    //@PostConstruct
    public void dataInit(){
        JoinUserDto joinUserDto = new JoinUserDto("asdf", "asdfasdf", "asdfasdf", "Tanguri");
        userService.saveUser(joinUserDto,"ROLE_ADMIN");
        //User user = JoinUserDto.toEntity(joinUserDto);
        //System.out.println(user.getImage().getUrl());
        //User user = new User("asdf","asdfasdf","asdf");
        //userRepository.save(user);
        JoinUserDto joinUserDto2 = new JoinUserDto("qwer", "qwerqwer", "qwerqwer", "Tanguri1");
        userService.saveUser(joinUserDto2,"ROLE_USER");
        Optional<User> optionalUser = userRepository.findByLoginId("asdf");
        User user = optionalUser.get();
        System.out.println("Init User Complete");
        for (int i = 0; i < 20; i++) {
            Content content = Content.builder()
                    .title(Integer.toString(i+1))
                    .texts(Integer.toString(i+1))
                    .user(user)
                    .password(user.getPassword())
                    .build();
            contentRepository.save(content);
        }
        System.out.println("Init content Complete");
    }

    //댓글 개수 초기화 작업
    //@PostConstruct
    @Transactional
    public void setCommentCnt(){
        List<Content> contents = contentRepository.findAll();
        for (Content content : contents) {
            int commentCnt = content.getComments().size();
            content.setCommentCnt(commentCnt);
            contentRepository.save(content);
        }
    }

}
