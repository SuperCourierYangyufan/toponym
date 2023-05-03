package com.example.toponym.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.toponym.entity.bean.User;
import com.example.toponym.entity.bean.UserDetailsInfo;
import com.example.toponym.entity.constant.YesOrNoConstant;
import com.example.toponym.entity.dto.UserDTO;
import com.example.toponym.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 @author 杨宇帆
 @create 2023-04-12
 根据用户名查用户
 */
@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User user = userService.getOne(new LambdaQueryWrapper<User>()
                                                   .eq(User::getAccount, username));
        if(user == null){
            throw new UsernameNotFoundException("该用户不存在");
        }
        UserDetailsInfo userDetailsInfo = new UserDetailsInfo();
        userDetailsInfo.setUser(BeanUtil.copyProperties(user, UserDTO.class));
        return userDetailsInfo;
    }
}
