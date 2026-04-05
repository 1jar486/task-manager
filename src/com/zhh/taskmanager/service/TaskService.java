package com.zhh.taskmanager.service;
import com.zhh.taskmanager.model.Task;

import java.util.List;

public interface TaskService {
    void addTask(Task task);
    List<Task> getAllTasks();
    void completeTask(int id);
    void deleteTask(int id);

}
