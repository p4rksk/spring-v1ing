package shop.mtcoding.blog._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration// 컴퍼넌트 스캔이되며 (설정파일에 역할을 하는 bin이다.)
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder(); //IOC등록이랑 sequrity
    }

    @Bean
    public WebSecurityCustomizer ignore() { //정적 자원 security filter에서 제외 시키기
        return web -> {
            web.ignoring().requestMatchers("/board/*", "/static/**", "/h2-console/**");//막을꺼 안막는 주소들(좋은방법은 아님)
        };
    }

    @Bean
//Security를 건너 뛰게 만들기(로그인 방화벽 해제)
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf(c -> c.disable());

        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers(RegexRequestMatcher.regexMatcher("/board/\\d+")).permitAll()
                    .requestMatchers("/user/**", "/board/**").authenticated()
                    .anyRequest().permitAll();
            // 인증이 필요한 페이지 설정 / .anyRequest().permitAll();는 인증이 필요한 페이지가 자연적으로 로고인 페이지로 redirection되게 설정

        });

        http.formLogin(f -> {
            f.loginPage("/loginForm").loginProcessingUrl("/login").defaultSuccessUrl("/").failureUrl("/loginForm");  // 우리가 만드 로그인폼 페이지로 가는데, .loginProcessingUrl("/login");을 붙이면 sf에 로그인 시스템을 사용한다.
        });
        return http.build();
    }


}
