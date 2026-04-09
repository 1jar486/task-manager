package com.zhh.taskmanager.repository;

import com.zhh.taskmanager.model.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
/**
 * 使用 @Mapper 注解交给 Spring Boot 扫描
 * 这里体现了 IoC（控制反转）：你不需要自己 new 实现类，Spring 会帮你生成并注入
 */
@Mapper
public interface UserRepository {

    // 查询：根据用户名找用户
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    // 插入：保存新用户
    @Insert("INSERT INTO user(username, password) VALUES(#{username}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 让数据库自动生成的 ID 回填到 user 对象里
    int save(User user);
}
