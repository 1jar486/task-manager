package com.zhh.taskmanager.repository;

import com.zhh.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

// 继承这个类，Spring 会自动帮你写好增删改查的代码
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
