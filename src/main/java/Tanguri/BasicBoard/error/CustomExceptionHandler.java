package Tanguri.BasicBoard.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice//컨트롤러 전역에서 발생하는 Custom Error를 잡아줄 Handler.
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)//우리가 만든 CustomException.class가 들어오면 @ExceptionHandler 실행
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e){//ResponseEntity에 ErrorResponseEntity를 넣어서 반환
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }
}
