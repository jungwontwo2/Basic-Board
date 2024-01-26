package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.dto.image.ImageResponseDto;
import Tanguri.BasicBoard.domain.dto.image.ImageUploadDto;
import Tanguri.BasicBoard.domain.entity.Image;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.ImageRepository;
import Tanguri.BasicBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    @Value("${file.profileImagePath}")
    private String uploadFolder;

    public void upload(ImageUploadDto imageUploadDto,String loginId){
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        User user = optionalUser.get();
        MultipartFile file = imageUploadDto.getFile();

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + file.getOriginalFilename();

        File destinationFile = new File(uploadFolder + imageFileName);
        try {
            file.transferTo(destinationFile);
            Image image = imageRepository.findByUser(user);
            if(image!=null){
                //이미지가 있으면 이미 존재하는 url 업데이트
                image.updateUrl("/profileImages/"+imageFileName);
            }else {
                //이미지가 없으면 객체 생성 후 저장
                image = Image.builder()
                        .user(user)
                        .url("/profileImages/" + imageFileName)
                        .build();
            }
            imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageResponseDto findImage(String loginId){
        User user = userRepository.findByLoginId(loginId).orElse(null);
        Image image = imageRepository.findByUser(user);

        String defaultImageUrl="/profileImages/anonymous.png";

        if(image==null){
            return ImageResponseDto.builder()
                    .url(defaultImageUrl)
                    .build();
        }else {
            return ImageResponseDto.builder()
                    .url(image.getUrl())
                    .build();
        }
    }
}
