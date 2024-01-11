package Tanguri.BasicBoard.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentEditDto {
    private String title;
    private String texts;
}
