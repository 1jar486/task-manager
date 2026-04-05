package com.zhh.taskmanager.service;

import com.zhh.taskmanager.model.Task;
import com.zhh.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 声明此为业务逻辑层组件，交由 Spring 容器管理
public class TaskServiceImpl implements TaskService {

    public TaskServiceImpl(){}

    @Autowired // 依赖注入数据库操作接口
    private TaskRepository taskRepository;

    // 添加任务方法（方法名不变，参数变为 Task 对象）
    @Override
    public void addTask(Task task) {
        // 前端传来的 task 对象已经包含了 title, priority 和 tag
        // 我们只需要强制设置其初始完成状态为 false
        task.setCompleted(false);
        // 调用 JPA 自动生成的 save 方法存入数据库
        taskRepository.save(task);
        // 打印系统日志确认
        System.out.println("系统消息：任务 [" + task.getTitle() + "] 已保存，优先级：" + task.getPriority());
    }

    // 获取所有任务（保持不变）
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // 切换任务状态（包含之前修复的取反逻辑，保持不变）
    @Override
    public void completeTask(int id) {
        taskRepository.findById(id).ifPresent(task -> {
            task.setCompleted(!task.isCompleted());
            taskRepository.save(task);
            System.out.println("系统消息：任务 " + id + " 状态已切换");
        });
    }

    // 删除任务（保持不变）
    @Override
    public void deleteTask(int id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            System.out.println("系统消息：任务 " + id + " 已从数据库删除");
        } else {
            System.out.println("系统消息：删除失败，未找到任务");
        }
    }
}