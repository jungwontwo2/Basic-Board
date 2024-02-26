package Tanguri.BasicBoard.domain.dto.content;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.entity.Comment;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentWriteDto {

    //@NotBlank(message = "제목을 입력해주세요.")
    private String title;

    //@NotBlank(message = "내용을 입력해주세요")
    private String texts;


    public static Content toEntity(ContentWriteDto contentDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER) User user){
        System.out.println(contentDto.title);
        System.out.println(contentDto.texts);
        Content content = Content.builder()
                .title(contentDto.getTitle())
                .texts(contentDto.getTexts())
                .user(user)
                .writer(user.getNickname())
                .password(user.getPassword())
                .build();
        return content;
    }

}
