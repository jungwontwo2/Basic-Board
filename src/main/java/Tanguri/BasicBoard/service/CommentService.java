package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.dto.CommentRequestDto;
import Tanguri.BasicBoard.domain.dto.CommentResponseDto;
import Tanguri.BasicBoard.domain.entity.Comment;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.CommentRepository;
import Tanguri.BasicBoard.repository.ContentRepository;
import Tanguri.BasicBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;

    //댓글작성
    public Long writeComment(CommentRequestDto commentRequestDto, Long contentId, String userNickname){
        //사용자와 게시물을 찾음
        User user = userRepository.findByNickname(userNickname).orElse(null);
        Content content = contentRepository.findById(contentId).orElse(null);
        //Comment.builder를 통해서 comment를 생성함
        //comment에는 commentRequestDto.getComment()를 통한 댓글내용
        //content에는 어떤 게시물에 관한건지에 대한 정보(content_id가 조인컬럼 되어있음)
        //user에는 어떤 유저가 작성했는지에 대한 정보(user_id가 조인컬럼 되어있음)
        Comment comment = Comment.builder()
                .comment(commentRequestDto.getComment())
                .content(content)
                .user(user)
                .build();
        //comment를 만들었다면 save함
        commentRepository.save(comment);
        return comment.getId();
    }
    //게시물에 대한 모든 댓글들 가져오기 위한 메서드
    public List<CommentResponseDto> commentDtoList(Long boardId){
        //contentRepository에서 boardId로 게시물을 가져옴 -> .get으로 content 추출
        Optional<Content> optionalContent = contentRepository.findById(boardId);
        Content content = optionalContent.get();
        //findByContent를 통해서 content를 넣어 해당 게시물의 댓글들을 comments에 넣는다.
        List<Comment> comments = commentRepository.findByContent(content);
        //해당 댓글들은 entity이기 때문에 CommentResponseDto로 바꿔준다(comment를 넣으면 commentResponseDto로 변환시켜줌).
        List<CommentResponseDto> collect = comments.stream().map(comment -> new CommentResponseDto(comment)).toList();
        return collect;
    }

    public void updateComment(CommentResponseDto commentResponseDto, Long commentId){
        Comment comment = commentRepository.findById(commentId).orElse(null);
        assert comment != null;
        comment.update(commentResponseDto.getComment());
        commentRepository.save(comment);
    }
    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }
}
