package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.entity.Image;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.ImageRepository;
import Tanguri.BasicBoard.repository.UserRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String saveProfileImage(MultipartFile multipartFile,String loginId) throws IOException {
        String filename = getUUIDFileName(multipartFile);
        ObjectMetadata metadata = setObjectMetadata(multipartFile);
        amazonS3.putObject(bucket, filename, multipartFile.getInputStream(), metadata);
        imageRepositoryUpdateImage(loginId, filename);
        return amazonS3.getUrl(bucket, filename).toString();
    }

    //ObjectMetadata 세팅후 반환
    private static ObjectMetadata setObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }
    //UUID를 통해 이름 설정
    private static String getUUIDFileName(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String filename = uuid+"_"+originalFilename;
        return filename;
    }

    //imageRepository를 통해서 프로필사진의 url을 update해줌
    private void imageRepositoryUpdateImage(String loginId, String filename) {
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        User user = optionalUser.get();
        Image image = imageRepository.findByUser(user);

        String imageUrl = "https://"+bucket + ".s3." + region + ".amazonaws.com/" + filename;
        image.updateUrl(imageUrl);
        //
        imageRepository.save(image);//얘가 사실 update를 해주는 듯하다.
        System.out.println("image.getUrl() = " + image.getUrl());
    }

    public void deleteImage(String originalFilename)  {
        amazonS3.deleteObject(bucket, originalFilename);
    }
}
