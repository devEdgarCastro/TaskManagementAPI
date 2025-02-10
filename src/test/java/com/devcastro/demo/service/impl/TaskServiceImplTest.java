package com.devcastro.demo.service.impl;

import com.devcastro.demo.entity.Task;
import com.devcastro.demo.entity.TaskStatus;
import com.devcastro.demo.exception.ResourceNotFoundException;
import com.devcastro.demo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTasks_returnsReversedListOfTasks() {
        List<Task> tasks = List.of(new Task("1", "Task 1", "Description 1", TaskStatus.TODO),
                new Task("2", "Task 2", "Description 2", TaskStatus.IN_PROGRESS));
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals("Task 2", result.get(0).getTitle());
        assertEquals("Task 1", result.get(1).getTitle());
    }

    @Test
    void createTask_savesAndReturnsTask() {
        Task task = new Task("1", "Task 1", "Description 1", TaskStatus.TODO);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task);

        assertEquals("Task 1", result.getTitle());
    }

    @Test
    void updateTaskStatus_updatesAndReturnsTask() {
        Task task = new Task("1", "Task 1", "Description 1", TaskStatus.TODO);
        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Optional<Task> result = taskService.updateTaskStatus("1", TaskStatus.IN_PROGRESS);

        assertTrue(result.isPresent());
        assertEquals(TaskStatus.IN_PROGRESS, result.get().getStatus());
    }

    @Test
    void updateTaskStatus_taskNotFound_throwsException() {
        when(taskRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTaskStatus("1", TaskStatus.IN_PROGRESS));
    }

    @Test
    void deleteTask_deletesTask() {
        when(taskRepository.existsById(anyString())).thenReturn(true);
        doNothing().when(taskRepository).deleteById(anyString());

        taskService.deleteTask("1");

        verify(taskRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteTask_taskNotFound_throwsException() {
        when(taskRepository.existsById(anyString())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask("1"));
    }
}