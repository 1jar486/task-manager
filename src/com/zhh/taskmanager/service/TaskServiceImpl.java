package com.zhh.taskmanager.service;

import com.zhh.taskmanager.model.Task;
import com.zhh.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void addTask(Task task) {
        task.setCompleted(false);
        taskRepository.insert(task);
        System.out.println("任务已保存: " + task.getTitle());
    }

    @Override
    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public void completeTask(int id, Long userId) {
        taskRepository.toggleStatus(id, userId);
    }

    @Override
    public void deleteTask(int id, Long userId) {
        taskRepository.deleteById(id, userId);
    }
}