package Tanguri.BasicBoard.domain.dto.content;

import Tanguri.BasicBoard.domain.SessionConst;
import Tanguri.BasicBoard.domain.dto.user.CustomUserDetails;
import Tanguri.BasicBoard.domain.entity.Comment;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.validator.ByteSize;
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

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @ByteSize(max = 500,message = "내용은 500바이트를 넘길 수 없습니다.")
    private String texts;


    public static Content toEntity(ContentWriteDto contentDto, CustomUserDetails user){
        Content content = Content.builder()
                .title(contentDto.getTitle())
                .texts(contentDto.getTexts())
                .user(user.getUserEntity())
                .writer(user.getNickname())
                .password(user.getPassword())
                .isImportant(false)
                .build();
        return content;
    }
    public static Content toImportantEntity(ContentWriteDto contentDto, CustomUserDetails user){
        Content content = Content.builder()
                .title(contentDto.getTitle())
                .texts(contentDto.getTexts())
                .user(user.getUserEntity())
                .writer(user.getNickname())
                .password(user.getPassword())
                .isImportant(true)
                .build();
        return content;
    }
}
