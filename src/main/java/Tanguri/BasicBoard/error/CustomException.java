package Tanguri.BasicBoard.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    //에러코드를 가지고 있는 CustomException 클래스이다.
    //나중에 에러가 생기면 new CustomException(ErrorCode.USER_NOT_FOUND); 이런식으로 만들어서 쓰면 된다.
    ErrorCode errorCode;
}
