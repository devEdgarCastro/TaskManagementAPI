package com.devcastro.demo.controller;

import com.devcastro.demo.dto.TaskDTO;
import com.devcastro.demo.entity.Task;
import com.devcastro.demo.entity.TaskStatus;
import com.devcastro.demo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTasks_returnsListOfTasks() {
        List<Task> tasks = List.of(new Task("1", "Task 1", "Description 1", TaskStatus.TODO));
        when(taskService.getAllTasks()).thenReturn(tasks);

        List<TaskDTO> result = taskController.getAllTasks();

        assertEquals(1, result.size());
        assertEquals("Task 1", result.getFirst().getTitle());
    }

    @Test
    void createTask_createsAndReturnsTask() {
        TaskDTO taskDTO = new TaskDTO(null, "Task 1", "Description 1", TaskStatus.TODO);
        Task task = new Task("1", "Task 1", "Description 1", TaskStatus.TODO);
        when(taskService.createTask(any(Task.class))).thenReturn(task);

        ResponseEntity<TaskDTO> response = taskController.createTask(taskDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Task 1", Objects.requireNonNull(response.getBody()).getTitle());
    }

    @Test
    void updateTaskStatus_updatesAndReturnsTask() {
        Task task = new Task("1", "Task 1", "Description 1", TaskStatus.TODO);
        when(taskService.updateTaskStatus(anyString(), any(TaskStatus.class))).thenReturn(Optional.of(task));

        ResponseEntity<TaskDTO> response = taskController.updateTaskStatus("1", TaskStatus.IN_PROGRESS);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(TaskStatus.TODO, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    void updateTaskStatus_taskNotFound() {
        when(taskService.updateTaskStatus(anyString(), any(TaskStatus.class))).thenReturn(Optional.empty());

        ResponseEntity<TaskDTO> response = taskController.updateTaskStatus("1", TaskStatus.IN_PROGRESS);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void deleteTask_deletesTask() {
        doNothing().when(taskService).deleteTask(anyString());

        ResponseEntity<Void> response = taskController.deleteTask("1");

        assertEquals(204, response.getStatusCode().value());
    }
}