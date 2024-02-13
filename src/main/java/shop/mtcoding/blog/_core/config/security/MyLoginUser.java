package shop.mtcoding.blog._core.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import shop.mtcoding.blog.user.User;

import java.util.Collection;
// Security 에서 자체로 session에 담는 시스템 구현 (세션에 저장 되는 오브젝트)

@RequiredArgsConstructor
@Getter
public class MyLoginUser implements UserDetails {

    private final User user;

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    //권한인데 아직 안배움
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


}
