package com.devcastro.demo.controller;

import com.devcastro.demo.dto.TaskDTO;
import com.devcastro.demo.entity.Task;
import com.devcastro.demo.entity.TaskStatus;
import com.devcastro.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/tasks")
@Tag(name = "Task", description = "API for managing tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    public List<TaskDTO> getAllTasks() {
        log.info("Getting all tasks");
        return taskService.getAllTasks().stream()
                .map(task -> new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getStatus()))
                .toList();
    }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Create a new task with the provided details")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        log.info("Creating task: {}", taskDTO);
        Task task = Task.builder().title(taskDTO.getTitle()).description(taskDTO.getDescription()).status(TaskStatus.TODO).build();
        Task createdTask = taskService.createTask(task);
        TaskDTO createdTaskDTO = new TaskDTO(createdTask.getId(), createdTask.getTitle(), createdTask.getDescription(), createdTask.getStatus());
        return ResponseEntity.ok(createdTaskDTO);
    }

    @PutMapping("/{id}/{status}")
    @Operation(summary = "Update task status", description = "Update the status of an existing task")
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable String id, @PathVariable TaskStatus status) {
        log.info("Updating task status: {} to {}", id, status);
        return taskService.updateTaskStatus(id, status)
                .map(task -> new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getStatus()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Delete an existing task by its ID")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        log.info("Deleting task: {}", id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}