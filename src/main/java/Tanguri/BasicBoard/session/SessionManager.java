package Tanguri.BasicBoard.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME="mySessionId";

    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    //세션 생성
    public void createSession(Object value, HttpServletResponse response){
        //세션 id 생성하고, 값을 세션에 저장
        //key:sessionId,value:Object
        //세션스토어에 UUID한 id를 넣고 그에 해당하는 값으로 Object를 넣는다
        //이 프로젝트에서는 User 엔티티가 들어간다.
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId,value);

        //쿠키 생성(key:SESSION_COOKIE_NAME(mySessionId),value:sessionId)
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME,sessionId);
        response.addCookie(mySessionCookie);
        System.out.println("make session complete");
    }

    //세션 조회
    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie==null){
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());
    }

    private Cookie findCookie(HttpServletRequest request, String cookieName) {
        if(request.getCookies()==null){
            return null;
        }
        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(cookieName)).findAny().orElse(null);
    }
}
