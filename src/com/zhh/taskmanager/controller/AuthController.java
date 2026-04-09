package com.zhh.taskmanager.controller;

import com.zhh.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController // 组合注解，包含 @Controller 和 @ResponseBody
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    // 1. 登录方法修改
    @PostMapping("/login")
    public String login(@RequestBody com.zhh.taskmanager.model.User user) {
        // 从 user 对象里拿账号密码
        return userService.login(user.getUsername(), user.getPassword());
    }

    @PostMapping("/register")
    // 必须加 @RequestBody，否则拿到的 username 为空
    public String register(@RequestBody com.zhh.taskmanager.model.User user){
        if(user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return "注册失败：用户名不能为空";
        }
        return userService.register(user.getUsername(), user.getPassword());
    }
}