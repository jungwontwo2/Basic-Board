package Tanguri.BasicBoard.domain.dto.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class BoardImageUploadDTO {
    private List<MultipartFile> files;
}
