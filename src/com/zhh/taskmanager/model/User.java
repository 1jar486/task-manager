package com.zhh.taskmanager.model;



/**
 * 用户实体类
 * 对应数据库中的 user 表
 */

public class User {
    private Long id;
    private String username;
    private String password;

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }
    public String getPassword(){
        return password;
    }
    public String getUsername() {
        return username;
    }
}
