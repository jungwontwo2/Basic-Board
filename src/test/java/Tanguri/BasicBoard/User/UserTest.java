package Tanguri.BasicBoard.User;

import Tanguri.BasicBoard.domain.dto.user.JoinUserDto;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.error.CustomException;
import Tanguri.BasicBoard.repository.UserRepository;
import Tanguri.BasicBoard.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class UserTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Validator validator;
    @Test
    public void JoinUserLoginIdShortFail(){
        JoinUserDto joinUserDto = new JoinUserDto("q","qwerqwer","qwerqwer","qwerr");
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(joinUserDto); //validator를 통해서 @Validate 실행한것과 같은 결과
        assertFalse(violations.isEmpty());//만약 validate를 통해서 에러가 있으면 violations안에는 error가 있다. 그러니까 isEmpty는 False가 된다.
        for (ConstraintViolation<JoinUserDto> violation : violations) {
            System.out.println("violation.getMessage() = " + violation.getMessage());
        }
        boolean hasLoginIdSizeViolation = violations.stream()//violations안에 돌면서
                .anyMatch(violation -> "loginId".equals(violation.getPropertyPath().toString())//검증 실패한 필드의 이름이 loginId인지 체크
                        && violation.getMessage().contains("최소 4자 이상"));//검증 실패 메세지에 '최소 4가 이상'이라는 문자열이 들어있는지
        assertTrue(hasLoginIdSizeViolation);
    }
    @Test
    public void JoinUserLoginIdLongFail(){
        JoinUserDto joinUserDto = new JoinUserDto("qqqqqqqqqqqqqqq","qwerqwer","qwerqwer","qwerr");
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(joinUserDto); //validator를 통해서 @Validate 실행한것과 같은 결과
        assertFalse(violations.isEmpty());//만약 validate를 통해서 에러가 있으면 violations안에는 error가 있다. 그러니까 isEmpty는 False가 된다.
        boolean hasLoginIdSizeViolation = violations.stream()//violations안에 돌면서
                .anyMatch(violation -> "loginId".equals(violation.getPropertyPath().toString())//검증 실패한 필드의 이름이 loginId인지 체크
                        && violation.getMessage().contains("10자 이하"));//검증 실패 메세지에 '최소 4가 이상'이라는 문자열이 들어있는지
        assertTrue(hasLoginIdSizeViolation);
    }
    @Test
    public void JoinUserPatternFail(){
        JoinUserDto joinUserDto = new JoinUserDto("!!@!","qwerqwer","qwerqwer","qwerr");
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(joinUserDto); //validator를 통해서 @Validate 실행한것과 같은 결과
        assertFalse(violations.isEmpty());//만약 validate를 통해서 에러가 있으면 violations안에는 error가 있다. 그러니까 isEmpty는 False가 된다.
        boolean hasLoginIdSizeViolation = violations.stream()//violations안에 돌면서
                .anyMatch(violation -> "loginId".equals(violation.getPropertyPath().toString())//검증 실패한 필드의 이름이 loginId인지 체크
                        && violation.getMessage().contains("알파벳 소문자(a~z), 숫자(0~9)만 입력 가능합니다."));//검증 실패 메세지에 '최소 4가 이상'이라는 문자열이 들어있는지
        assertTrue(hasLoginIdSizeViolation);
    }
    @Test
    public void JoinUserSameLoginIdFail(){
        JoinUserDto joinUserDto = new JoinUserDto("qwerr","qwerqwer","qwerqwer","qwerr");
        userService.saveUser(joinUserDto,"ROLE_USER");
        CustomException exception = assertThrows(CustomException.class,
                ()->{userService.saveUser(joinUserDto,"ROLE_USER");});
        assertEquals("해당 ID가 중복됩니다.",exception.getErrorCode().getMessage());
    }
    @Test
    public void JoinUserSameNicknameFail(){
        JoinUserDto joinUserDto = new JoinUserDto("qwerr","qwerqwer","qwerqwer","qwerr");
        JoinUserDto joinUserDto2 = new JoinUserDto("qwef","qwerqwer","qwerqwer","qwerr");
        userService.saveUser(joinUserDto,"ROLE_USER");
        CustomException exception = assertThrows(CustomException.class,
                ()->{userService.saveUser(joinUserDto2,"ROLE_USER");});
        assertEquals("닉네임이 중복됩니다.",exception.getErrorCode().getMessage());
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
        entityManager.clear();
        User user = userRepository.findByLoginId("qwerr").orElse(null);
        userService.deleteUser(user.getId());
        User user1 = userRepository.findByLoginId("qwerr").orElse(null);
        Assertions.assertThat(user1).isNull();
    }
}
