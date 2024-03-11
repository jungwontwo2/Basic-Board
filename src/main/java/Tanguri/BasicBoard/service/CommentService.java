package Tanguri.BasicBoard.service;

import Tanguri.BasicBoard.domain.dto.comment.CommentRequestDto;
import Tanguri.BasicBoard.domain.dto.comment.CommentResponseDto;
import Tanguri.BasicBoard.domain.dto.content.ContentDto;
import Tanguri.BasicBoard.domain.dto.user.CustomUserDetails;
import Tanguri.BasicBoard.domain.entity.Comment;
import Tanguri.BasicBoard.domain.entity.Content;
import Tanguri.BasicBoard.domain.entity.User;
import Tanguri.BasicBoard.repository.CommentRepository;
import Tanguri.BasicBoard.repository.ContentRepository;
import Tanguri.BasicBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;

    //댓글작성
    public Long writeComment(CommentRequestDto commentRequestDto, Long contentId, String parentId, Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUserEntity();
        //사용자와 게시물을 찾음
        Content content = contentRepository.findById(contentId).orElse(null);
        //부모 댓글을 찾는 과정
        //일단 부모가 없다고 설정함
        Comment parentComment = null;
        //만약 parentId가 null이 아니면 부모 댓글이 있다는 뜻
//        if(!Objects.equals(parentId, "")){
        if(parentId!=null){
            long parentIdLong = Long.parseLong(parentId);
            //commentRepository에서 parentIdLong을 통해서 부모 댓글을 찾는다.
            parentComment = commentRepository.findById(parentIdLong).orElse(null);
        }
        //Comment.builder를 통해서 comment를 생성함
        //comment에는 commentRequestDto.getComment()를 통한 댓글내용
        //content에는 어떤 게시물에 관한건지에 대한 정보(content_id가 조인컬럼 되어있음)
        //user에는 어떤 유저가 작성했는지에 대한 정보(user_id가 조인컬럼 되어있음)
        Comment comment = Comment.builder()
                .comment(commentRequestDto.getComment())
                .content(content)
                .user(user)
                .parent(parentComment)//부모 댓글 설정
                .build();
        //comment를 만들었다면 save함
        commentRepository.save(comment);
        //contentRepository.save(content);
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

    public Page<CommentResponseDto> paging(Pageable pageable) {
        int page=pageable.getPageNumber()-1;//page위치에 있는 값은 0부터 시작한다.
        int pageLimit = 8;//한페이지에 보여줄 글 개수
        //System.out.println("zz");
        Page<Comment> comments = commentRepository.findAll(PageRequest.of(page, pageLimit, Sort.Direction.DESC, "id"));
        Page<CommentResponseDto> commentsDto = comments.map(comment -> new CommentResponseDto(comment));
        return commentsDto;
    }
}
