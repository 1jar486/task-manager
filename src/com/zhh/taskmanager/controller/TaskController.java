package com.zhh.taskmanager.controller;

import com.zhh.taskmanager.model.Task;
import com.zhh.taskmanager.service.TaskService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // 声明此为 RESTful 控制器，所有方法返回 JSON
@RequestMapping("/api/tasks") // 配置基础路由映射
@CrossOrigin // 允许跨域请求
public class TaskController {

    private final TaskService service; // 声明业务层接口

    // 构造器注入
    public TaskController(TaskService service) {
        this.service = service;
    }

    // 查询所有（保持不变）
    @GetMapping
    public List<Task> getAll() {
        return service.getAllTasks();
    }

    // 【修改】添加数据方法。参数使用 @RequestBody 将前端发来的 JSON 直接转换为 Task 对象
    @PostMapping
    public void add(@RequestBody Task task) {
        service.addTask(task); // 调用 Service 层的添加方法
    }

    // 更新状态（保持不变）
    @PutMapping("/{id}")
    public void complete(@PathVariable int id) {
        service.completeTask(id);
    }

    // 删除数据（保持不变）
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.deleteTask(id);
    }
}