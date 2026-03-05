package com.taskmanager.taskmanager;

import com.taskmanager.taskmanager.model.Task;
import com.taskmanager.taskmanager.repository.TaskRepository;
import com.taskmanager.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task();
        sampleTask.setId(1L);
        sampleTask.setTitle("Test Task");
        sampleTask.setDescription("Test Description");
        sampleTask.setStatus(Task.TaskStatus.TODO);
    }

    // ── US-001: View All Tasks ──

    @Test
    void getAllTasks_shouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(sampleTask));
        List<Task> tasks = taskService.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getAllTasks_shouldReturnEmptyList_whenNoTasks() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());
        List<Task> tasks = taskService.getAllTasks();
        assertTrue(tasks.isEmpty());
        verify(taskRepository, times(1)).findAll();
    }

    // ── US-002: Create a Task ──

    @Test
    void createTask_shouldSaveAndReturnTask() {
        when(taskRepository.save(sampleTask)).thenReturn(sampleTask);
        Task created = taskService.createTask(sampleTask);
        assertNotNull(created);
        assertEquals("Test Task", created.getTitle());
        verify(taskRepository, times(1)).save(sampleTask);
    }

    // ── US-003: Edit a Task ──

    @Test
    void updateTask_shouldUpdateFieldsAndReturnTask() {
        Task updatedData = new Task();
        updatedData.setTitle("Updated Title");
        updatedData.setDescription("Updated Description");
        updatedData.setStatus(Task.TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        Task result = taskService.updateTask(1L, updatedData);
        assertNotNull(result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_shouldThrowException_whenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
            () -> taskService.updateTask(99L, sampleTask));
    }

    // ── US-004: Delete a Task ──

    @Test
    void deleteTask_shouldCallDeleteById() {
        doNothing().when(taskRepository).deleteById(1L);
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void getTaskById_shouldThrowException_whenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
            () -> taskService.getTaskById(99L));
    }

    // ── US-005: Filter Tasks by Status ──

    @Test
    void getTasksByStatus_shouldReturnMatchingTasks() {
        when(taskRepository.findByStatus(Task.TaskStatus.TODO))
            .thenReturn(Arrays.asList(sampleTask));
        List<Task> tasks = taskService.getTasksByStatus(Task.TaskStatus.TODO);
        assertEquals(1, tasks.size());
        assertEquals(Task.TaskStatus.TODO, tasks.get(0).getStatus());
    }

    @Test
    void getTasksByStatus_shouldReturnEmptyList_whenNoMatches() {
        when(taskRepository.findByStatus(Task.TaskStatus.DONE))
            .thenReturn(Collections.emptyList());
        List<Task> tasks = taskService.getTasksByStatus(Task.TaskStatus.DONE);
        assertTrue(tasks.isEmpty());
    }
}