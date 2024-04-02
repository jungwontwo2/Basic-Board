package Tanguri.BasicBoard.controller;

import Tanguri.BasicBoard.domain.dto.image.ImageUploadDto;
import Tanguri.BasicBoard.domain.dto.user.CustomUserDetails;
import Tanguri.BasicBoard.service.ImageService;
import Tanguri.BasicBoard.service.S3UploadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    private final S3UploadService s3UploadService;

    @ExceptionHandler(FileSizeLimitExceededException.class)
    private String filesizelimit(HttpServletRequest request){
        request.setAttribute("msg","이미지 크기는 1Mb 이하여야합니다");
        request.setAttribute("redirectUrl","/users/my/edit/image");
        return "common/messageRedirect";
    }


    @PostMapping("/upload")
    public String upload(@ModelAttribute ImageUploadDto imageUploadDTO, HttpServletRequest request,
                         Authentication authentication) throws IOException {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        HttpSession session = request.getSession(false);
        if(session==null){
            request.setAttribute("msg","로그인 후 사용 가능합니다.");
            request.setAttribute("redirectUrl","/users/login");
            return "common/messageRedirect";
        }

        s3UploadService.saveProfileImage(imageUploadDTO.getFile(), user.getUsername());

        return "redirect:/users/my";
    }
}
