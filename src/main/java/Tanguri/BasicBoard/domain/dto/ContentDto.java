package Tanguri.BasicBoard.domain.dto;

import jakarta.validation.constraints.NotEmpty;

public class ContentDto {

    @NotEmpty
    private String title;
    @NotEmpty
    private String texts;
}
