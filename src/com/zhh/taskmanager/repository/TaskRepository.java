package com.zhh.taskmanager.repository;

import com.zhh.taskmanager.model.Task;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface TaskRepository {

    @Select("SELECT * FROM task WHERE user_id = #{userId}")
    List<Task> findByUserId(Long userId);

    @Insert("INSERT INTO task (user_id, title, completed, priority, tag) " +
            "VALUES (#{userId}, #{title}, #{completed}, #{priority}, #{tag})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Task task);

    @Update("UPDATE task SET completed = NOT completed WHERE id = #{id} AND user_id = #{userId}")
    void toggleStatus(@Param("id") int id, @Param("userId") Long userId);

    @Delete("DELETE FROM task WHERE id = #{id} AND user_id = #{userId}")
    void deleteById(@Param("id") int id, @Param("userId") Long userId);
}