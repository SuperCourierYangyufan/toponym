package com.example.toponym.controller;

import com.example.toponym.entity.dto.UserDTO;
import com.example.toponym.service.UserService;
import com.example.toponym.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 @author 杨宇帆
 @create 2023-04-12
 */
@Api(tags = "用户登陆|注册")
@RestController
@Slf4j
@RequestMapping("/auth")
public class UserLoginController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录接口",notes = "登录接口,无需token")
    @PostMapping("/login")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "account",value = "账号",required = true,paramType = "body"),
            @ApiImplicitParam(name = "password",value = "密码",required = true,paramType = "body")
    })
    public R login(@RequestBody UserDTO userDTO, HttpServletRequest request) throws Exception{
        return R.data(userService.login(userDTO,request));
    }

    /**
     * 获取验证码
     * @param mobile
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取验证码",notes = "获取验证码")
    @GetMapping("/getSmsCode")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "account",value = "账号",required = true,paramType = "body"),
    })
    public R getSmsCode(String mobile) throws Exception{
        userService.getSmsCode(mobile);
        return R.success();
    }

    /**
     * 外部用户注册
     * @param userDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/register")
    @ApiOperation(value = "外部用户注册",notes = "外部用户注册")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "account",value = "账号",required = true,paramType = "body"),
            @ApiImplicitParam(name = "password",value = "密码",required = true,paramType = "body"),
            @ApiImplicitParam(name = "mobile",value = "手机号",required = true,paramType = "body"),
            @ApiImplicitParam(name = "smsCode",value = "手机号验证码,现在暂无,传入1234即可",required = true,paramType = "body")
    })
    public R register(@RequestBody UserDTO userDTO, HttpServletRequest request) throws Exception{
        return R.data(userService.register(userDTO,request));
    }

}
