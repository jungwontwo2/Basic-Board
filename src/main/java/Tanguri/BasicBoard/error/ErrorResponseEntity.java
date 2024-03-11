package Tanguri.BasicBoard.error;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponseEntity {
    private int status;
    private String name;
    private String code;
    private String message;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode e){
        //HTTP 요청에 대한 응답을 ResponseEntity로 표현한다.
        return ResponseEntity
                .status(e.getHttpStatus())//상태를 가져와서 설정함
                .body(ErrorResponseEntity.builder()//여기서 ErrorResponseEntity를 생성
                        .status(e.getHttpStatus().value())//status의 값 404,500
                        .name(e.name())//이름 USER_NOT_FOUND
                        .code(e.getCode())//코드 ACCOUNT-001
                        .message(e.getMessage())//메세지 "사용자를 찾을 수 없습니다."
                        .build());
    }
}
