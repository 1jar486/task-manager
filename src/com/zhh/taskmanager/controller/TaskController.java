package com.zhh.taskmanager.controller;

import com.zhh.taskmanager.model.Task;
import com.zhh.taskmanager.service.TaskService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    private Long getUserIdFromToken(String token) {
        return Long.parseLong(token.replace("SUCCESS_TOKEN_FOR_", ""));
    }

    @GetMapping
    public List<Task> getAll(@RequestHeader("Authorization") String token) {
        return service.getTasksByUserId(getUserIdFromToken(token));
    }

    @PostMapping
    public void add(@RequestBody Task task, @RequestHeader("Authorization") String token) {
        task.setUserId(getUserIdFromToken(token));
        service.addTask(task);
    }

    @PutMapping("/{id}")
    public void toggle(@PathVariable int id, @RequestHeader("Authorization") String token) {
        service.completeTask(id, getUserIdFromToken(token));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id, @RequestHeader("Authorization") String token) {
        service.deleteTask(id, getUserIdFromToken(token));
    }
}