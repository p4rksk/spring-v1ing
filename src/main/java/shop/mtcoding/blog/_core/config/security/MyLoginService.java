package shop.mtcoding.blog._core.config.security;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog.user.User;
import shop.mtcoding.blog.user.UserRepository;


//조건 POST,/login, x-www.form-urlencoded, 키 값이 username , password
@RequiredArgsConstructor
@Service
public class MyLoginService implements UserDetailsService { //controllr에서 구현 했던 로그인 페이지 코드가 들어가야됨
    private final UserRepository userRepository;
    private final HttpSession session;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println();
        User user = userRepository.findByUsername(username);

        if (username == null) {
            System.out.println("user는 null");
            return null;
        } else {
            System.out.println("user를 찾았어요");
            session.setAttribute("session",user); // 머스테치에서 사용하려고
            return new MyLoginUser(user); //SecurityContextHolder 저장
        }
    }
}
