package Tanguri.BasicBoard.domain.dto.image;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageResponseDto {
    private String url;

    @Builder
    public ImageResponseDto(String url){
        this.url=url;
    }
}
