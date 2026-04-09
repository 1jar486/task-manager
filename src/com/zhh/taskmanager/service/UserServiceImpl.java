package com.zhh.taskmanager.service;

import com.zhh.taskmanager.model.User;
import com.zhh.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 这是一个类 (Class)
 * 它使用 implements 关键字来“执行”上面的 UserService 接口
 */
@Service // 【IoC 注解】：告诉 Spring 框架，“我是一个业务逻辑组件，请把我管理起来”
public class UserServiceImpl implements UserService{

    @Autowired // 【IoC 注解】：自动从 Spring 容器里找一个 UserRepository 的实现对象“塞”进来
    private UserRepository userRepository;

    @Override // 这里的注解表示正在重写接口中的 login 方法
    public String login(String username, String password) {
        // 1. 去数据库查这个名字的用户
        User user = userRepository.findByUsername(username);

        // 2. 逻辑判断
        if (user != null && user.getPassword().equals(password)) {
            // 登录成功，返回一个标识（实际项目中这里通常返回 JWT Token）
            return "SUCCESS_TOKEN_FOR_" + user.getId();
        }

        // 3. 失败返回提示
        return "登录失败:用户名或密码错误";
    }

    @Override
    public String register(String username, String password){
        // 1. 先查重
        User existUser = userRepository.findByUsername(username);
        if (existUser != null){
            return "注册失败：该用户名已被占用";
        }

        // 2. 创建对象
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        // 3. 【关键】调用 MyBatis 定义的 save 方法写入数据库
        int result = userRepository.save(newUser);

        if(result > 0) {
            return "注册成功";
        } else {
            return "注册失败：数据库写入异常";
        }
    }

}