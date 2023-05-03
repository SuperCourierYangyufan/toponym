package com.example.toponym.config;

import com.example.toponym.entity.bean.UserDetailsInfo;
import com.example.toponym.entity.constant.OtherInfoConstant;
import com.example.toponym.entity.constant.RedisConstant;
import com.example.toponym.entity.dto.UserDTO;
import com.example.toponym.service.OperateLogService;
import com.example.toponym.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 @author 杨宇帆
 @create 2023-04-13
 */
@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OperateLogService operateLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader(OtherInfoConstant.HEAD_TOKEN_NAME);
        if (StringUtils.isEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims;
        try {
            claims = JwtUtil.parseToken(token);
        } catch (Exception e) {
            log.error("解析token失败");
            throw new ServletException("token不合法");
        }
        Object userObj = redisTemplate.opsForValue().get(RedisConstant.LOGIN_USER + claims.getId());
        if (userObj == null) {
            throw new ServletException("登录已超时,请重新登录");
        }
        UserDetailsInfo userDetailsInfo = new UserDetailsInfo();
        userDetailsInfo.setUser((UserDTO) userObj);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetailsInfo, null,
                                                                                                          null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //拦截器日志
        operateLogService.addLog(request,(UserDTO) userObj);
        filterChain.doFilter(request,response);
    }
}
