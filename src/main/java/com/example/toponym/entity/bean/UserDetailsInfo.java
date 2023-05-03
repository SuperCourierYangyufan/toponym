package com.example.toponym.entity.bean;

import com.example.toponym.entity.constant.YesOrNoConstant;
import com.example.toponym.entity.dto.UserDTO;
import java.util.Collection;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 @author 杨宇帆
 @create 2023-04-12
 */
@Data
public class UserDetailsInfo implements UserDetails {
    private UserDTO user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
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
        return YesOrNoConstant.YES.equals(user.getUserStatus());
    }


}
