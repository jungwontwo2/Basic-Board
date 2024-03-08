package Tanguri.BasicBoard.config;

import Tanguri.BasicBoard.service.CustomUserDetailsService;
import Tanguri.BasicBoard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final AuthenticationFailureHandler CustomAuthFailureHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth.
                requestMatchers("/", "/users/login", "/users/join").permitAll()//해당 사이트면 모두 허용
                //.requestMatchers("/admin").hasRole("ADMIN")//admin페이지에는 ADMIN이라는 Role을 가지고 있어야 가능
                .requestMatchers("/users/my/**").hasAnyRole("ADMIN", "USER")//여기는 ADMIN이나 USER 둘중 아무거나 있으면 가능
                .anyRequest().authenticated()//나머지는 로그인 했으면 가능
        );

        http.formLogin((auth) -> auth.loginPage("/users/login")//로그인 페이지
                .loginProcessingUrl("/users/login")//포스트 보내면 어디로 가는지
                .usernameParameter("loginId")
                .failureHandler(CustomAuthFailureHandler)
//                .defaultSuccessUrl("/",true)
                .defaultSuccessUrl("/users/my",true)
                .permitAll());


        http.csrf((auth)->auth.disable());

        http.sessionManagement((auth) -> auth.maximumSessions(1).maxSessionsPreventsLogin(true));//true: 새로운 로그인 차단 false: 기존 세션 하나 삭제

        http.sessionManagement((session) -> session.sessionFixation((sessionFixation) -> sessionFixation.newSession()));//로그인 시 동일한 세션에 대한 id 변경


        return http.build();
    }




    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/css/**", "style.css");
    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http,
//                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
//                                                       CustomUserDetailsService userService) throws Exception {
//
//        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
//        sharedObject
//                .userDetailsService(userService) //사용자 정보 조회
//                .passwordEncoder(bCryptPasswordEncoder);
//
//        return sharedObject.build();
//    }

}
