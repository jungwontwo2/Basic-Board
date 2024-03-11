package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.Heart;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.error.CustomException;
import Tanguri.BasicBoard.error.ErrorCode;
import Tanguri.BasicBoard.repository.ContentRepository;
import Tanguri.BasicBoard.repository.HeartRepository;
import Tanguri.BasicBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    public void addHeart(String loginId,Long contentId) throws Exception {
        Content content = contentRepository.findById(contentId).get();
        User user = userRepository.findByLoginId(loginId).get();
        User boardUser = content.getUser();

        if(!boardUser.equals(user)){
            if(!checkLike(loginId,contentId)){
                content.heartChange(content.getHeartCnt()+1);
                Heart heart = Heart.builder()
                        .content(content)
                        .user(user).build();
                heartRepository.save(heart);
            }
        }
        throw new CustomException(ErrorCode.BOARD_OWNER);
    }

    public Boolean checkLike(String loginId, Long contentId) {
        return heartRepository.existsByUserLoginIdAndContentId(loginId, contentId);
    }
}
