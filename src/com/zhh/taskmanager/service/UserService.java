package com.zhh.taskmanager.service;

/**
 * 这是一个接口 (Interface)
 * 它像是一份“合同”，规定了登录功能需要哪些输入和输出
 */
public interface UserService {

    // 只声明方法名、参数和返回值类型
    String login(String username, String password);

    // 注册方法
    String register(String username, String password);

}