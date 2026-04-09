package com.zhh.taskmanager.service;
import com.zhh.taskmanager.model.Task;

import java.util.List;

public interface TaskService {
    void addTask(Task task);
    List<Task> getTasksByUserId(Long userId);
    void completeTask(int id, Long userIdFromToken);
    void deleteTask(int id, Long userIdFromToken);

}
