package com.devcastro.demo.service;

import com.devcastro.demo.entity.Task;
import com.devcastro.demo.entity.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getAllTasks();
    Task createTask(Task task);
    Optional<Task> updateTaskStatus(String taskId, TaskStatus status);
    void deleteTask(String taskId);
}