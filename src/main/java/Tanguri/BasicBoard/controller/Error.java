//package Tanguri.BasicBoard.controller;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.Date;
//
//@Controller
//public class Error implements ErrorController {
//
//    // 에러 페이지 정의
//    private final String ERROR_404_PAGE_PATH = "templates/error/404";
//    private final String ERROR_500_PAGE_PATH = "templates/error/500";
//    private final String ERROR_ETC_PAGE_PATH = "templates/error/error";
//
//
//    @RequestMapping("/templates/error")
//    public String handleError(HttpServletRequest request, Model model) {
//        //request.getAttribute로 에러 상태 코드를 받아온다. ReqeustDispatche.ERROR_STATUS_CODE는 HttpServletRequest의 속성중 하나
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        //HTTP 상태 코드는 정수 형태로 표현되니까 status.toString으로 문자열로 변환
//        //문자열로 바꾼것을 정수로 바꾸기 위해 Integer.valueOf를 통해 변환
//        //HttpStatus.valueOf는 주어진 정수 값에 해당하는 HttpStatus 상태를 반환
//        //예를들어 클라이언트 요청에서 받은 HTTP 상태 코드가 404면 HttpStatus.NOT_FOUND반환
//        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
//
//        if (status != null) {
//            int statusCode = Integer.valueOf(status.toString());
//            //클라이언트 에러
//            if (statusCode == HttpStatus.NOT_FOUND.value()) {
//                //상태 코드 반환
//                model.addAttribute("code", status.toString());
//                //404면 Not Found처럼 그에 해당하는 문구를 반환
//                model.addAttribute("msg", httpStatus.getReasonPhrase());
//                //시간
//                model.addAttribute("timeStamp", new Date());
//                return ERROR_404_PAGE_PATH;
//            }
//            //서버 에러
//            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                return ERROR_500_PAGE_PATH;
//            }
//        }
//        //기타 등등
//        return ERROR_ETC_PAGE_PATH;
//    }
//}