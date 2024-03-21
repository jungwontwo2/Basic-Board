package Tanguri.BasicBoard.User;

import Tanguri.BasicBoard.domain.dto.user.JoinUserDto;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.error.CustomException;
import Tanguri.BasicBoard.repository.UserRepository;
import Tanguri.BasicBoard.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
public class UserTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Test
    public void JoinUser(){
        JoinUserDto joinUserDto = new JoinUserDto("qwerr","qwerqwer","qwerqwer","qwerr");
        userService.saveUser(joinUserDto,"ROLE_USER");
    }
    @Test
    public void JoinUserException(){
        JoinUserDto joinUserDto = new JoinUserDto("qwerr","qwerqwer","qwerqwer","qwerr");
        userService.saveUser(joinUserDto,"ROLE_USER");
        CustomException exception = assertThrows(CustomException.class,
                ()->{userService.saveUser(joinUserDto,"ROLE_USER");});
        System.out.println("exception.getMessage() = " + exception.getMessage());
        assertEquals("해당 ID가 중복됩니다.",exception.getMessage());
    }
    @Test
    public void findById(){
        JoinUserDto joinUserDto = new JoinUserDto("qwerr","qwerqwer","qwerqwer","qwerr");
        userService.saveUser(joinUserDto,"ROLE_USER");
        User user = userRepository.findByLoginId("qwerr").orElse(null);
        Assertions.assertThat(user.getNickname()).isEqualTo("qwerr");
    }
    @Test
    public void deleteUserById(){
        JoinUserDto joinUserDto = new JoinUserDto("qwerr","qwerqwer","qwerqwer","qwerr");
        userService.saveUser(joinUserDto,"ROLE_USER");
        User user = userRepository.findByLoginId("qwerr").orElse(null);
        userService.deleteUser(user.getId());
    }
}
