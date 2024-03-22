package Tanguri.BasicBoard.Content;

import Tanguri.BasicBoard.domain.dto.content.ContentWriteDto;
import Tanguri.BasicBoard.domain.dto.image.BoardImageUploadDTO;
import Tanguri.BasicBoard.domain.dto.user.CustomUserDetails;
import Tanguri.BasicBoard.domain.dto.user.JoinUserDto;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.ContentRepository;
import Tanguri.BasicBoard.repository.UserRepository;
import Tanguri.BasicBoard.service.ContentService;
import Tanguri.BasicBoard.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class ContentTest {
    @Autowired
    ContentService contentService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    Validator validator;
    @Test
    public void writeContent() throws IOException {
        User user = userRepository.findByLoginId("qwer").orElse(null);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        ContentWriteDto contentWriteDto = new ContentWriteDto("asdf","asdfasdf");
        BoardImageUploadDTO boardImageUploadDTO = new BoardImageUploadDTO();
        contentService.writeContent(contentWriteDto,customUserDetails,boardImageUploadDTO);
        List<Content> allContents = contentService.getAllContents();
        Assertions.assertThat(allContents).hasSize(22);
    }
    @Test
    public void writeContentBlankFail() throws IOException {
        ContentWriteDto contentWriteDto = new ContentWriteDto("","");
        Set<ConstraintViolation<ContentWriteDto>> violations = validator.validate(contentWriteDto);
        assertFalse(violations.isEmpty());
        boolean blanktitle = violations.stream()//violations안에 돌면서
                .anyMatch(violation -> "title".equals(violation.getPropertyPath().toString())
                        && violation.getMessage().contains("제목을 입력해주세요.") );
        boolean blanktexts = violations.stream()//violations안에 돌면서
                .anyMatch(violation -> "texts".equals(violation.getPropertyPath().toString())
                        && violation.getMessage().contains("내용을 입력해주세요.") );
        assertTrue(blanktexts && blanktitle);
    }
    @Test
    public void writeContentToMuchTextsFail() throws IOException {
        String texts="";
        for (int i = 0; i < 30; i++) {
            texts+="ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ";
        }
        ContentWriteDto contentWriteDto = new ContentWriteDto("",texts);
        Set<ConstraintViolation<ContentWriteDto>> violations = validator.validate(contentWriteDto);
        assertFalse(violations.isEmpty());
        boolean ToMuchTexts = violations.stream()//violations안에 돌면서
                .anyMatch(violation -> "texts".equals(violation.getPropertyPath().toString())
                        && violation.getMessage().contains("내용은 500바이트를 넘길 수 없습니다.") );
        assertTrue(ToMuchTexts);
    }
    @Test
    public void deleteContent() throws IOException {
        User user = userRepository.findByLoginId("qwer").orElse(null);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        ContentWriteDto contentWriteDto = new ContentWriteDto("asdf","asdfasdf");
        BoardImageUploadDTO boardImageUploadDTO = new BoardImageUploadDTO();
        Long contentId = contentService.writeContent(contentWriteDto, customUserDetails, boardImageUploadDTO);
        contentService.deleteContent(contentId);
        Content content = contentRepository.findById(contentId).orElse(null);
        Assertions.assertThat(content).isNull();
    }
}
