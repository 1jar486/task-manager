package com.zhh.taskmanager.model;

import jakarta.persistence.*; // 如果报错，按 Alt+Enter 导入

@Entity // 告诉数据库：这是一个表
@Table(name = "tasks")
public class Task {

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 自动增长
    private Integer id; // 任务ID

    private String title; // 任务标题
    private boolean completed; // 任务完成状态

    // 【新增】任务优先级，例如："高", "中", "低"
    private String priority;

    // 【新增】任务标签，例如："学习", "生活", "兼职"
    private String tag;

    // 必须保留的无参构造方法，Hibernate 框架通过它来自动创建对象

    // 包含所有字段的构造方法（方便手动创建对象）
    public Task(String title, boolean completed, String priority, String tag) {
        this.title = title;
        this.completed = completed;
        this.priority = priority;
        this.tag = tag;
    }

    // --- 必须有这个：无参数构造方法（数据库需要） ---
    public Task() {
    }

    // --- 下面是 Getter 和 Setter 方法（必须有，否则报错） ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // 【新增】获取优先级
    public String getPriority() { return priority; }
    // 【新增】设置优先级
    public void setPriority(String priority) { this.priority = priority; }

    // 【新增】获取标签
    public String getTag() { return tag; }
    // 【新增】设置标签
    public void setTag(String tag) { this.tag = tag; }
}