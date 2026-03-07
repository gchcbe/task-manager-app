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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mockito.ArgumentCaptor;

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

    // ── US-006: Task Priority ──

    // AC8: createTask with HIGH priority saves and returns correct priority
    @Test
    void createTask_withHighPriority_savesAndReturnsCorrectPriority() {
        Task highPriorityTask = new Task();
        highPriorityTask.setTitle("Urgent Task");
        highPriorityTask.setPriority(Task.Priority.HIGH);

        when(taskRepository.save(highPriorityTask)).thenReturn(highPriorityTask);

        Task created = taskService.createTask(highPriorityTask);

        assertEquals(Task.Priority.HIGH, created.getPriority());
        verify(taskRepository, times(1)).save(highPriorityTask);
    }

    // AC9: createTask with no priority defaults to MEDIUM
    @Test
    void createTask_withNoPriority_defaultsToMedium() {
        Task taskWithoutPriority = new Task();
        taskWithoutPriority.setTitle("Task Without Priority");
        // priority intentionally not set — should default to MEDIUM

        when(taskRepository.save(taskWithoutPriority)).thenReturn(taskWithoutPriority);

        Task created = taskService.createTask(taskWithoutPriority);

        assertEquals(Task.Priority.MEDIUM, created.getPriority());
    }

    // AC10: updateTask can change priority from LOW to HIGH
    @Test
    void updateTask_canChangePriority_fromLowToHigh() {
        sampleTask.setPriority(Task.Priority.LOW);

        Task updatedData = new Task();
        updatedData.setTitle(sampleTask.getTitle());
        updatedData.setDescription(sampleTask.getDescription());
        updatedData.setStatus(sampleTask.getStatus());
        updatedData.setPriority(Task.Priority.HIGH);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(captor.capture())).thenReturn(sampleTask);

        taskService.updateTask(1L, updatedData);

        assertEquals(Task.Priority.HIGH, captor.getValue().getPriority());
    }

    // ── US-007: Dashboard ──

    // AC9: getOverdueTasks returns only tasks past due date with non-DONE status
    @Test
    void getOverdueTasks_shouldReturnOnlyOverdueNonDoneTasks() {
        Task overdueTask = new Task();
        overdueTask.setTitle("Overdue Task");
        overdueTask.setStatus(Task.TaskStatus.TODO);
        overdueTask.setDueDate(LocalDate.now().minusDays(2));

        when(taskRepository.findByDueDateBeforeAndStatusNot(
                any(LocalDate.class), eq(Task.TaskStatus.DONE)))
            .thenReturn(Arrays.asList(overdueTask));

        List<Task> overdue = taskService.getOverdueTasks();

        assertEquals(1, overdue.size());
        assertNotEquals(Task.TaskStatus.DONE, overdue.get(0).getStatus());
        assertTrue(overdue.get(0).getDueDate().isBefore(LocalDate.now()));
    }

    // AC10: getTaskCountByStatus returns correct counts per status
    @Test
    void getTaskCountByStatus_shouldReturnCorrectCountsPerStatus() {
        Task todoTask1 = new Task();
        todoTask1.setStatus(Task.TaskStatus.TODO);

        Task todoTask2 = new Task();
        todoTask2.setStatus(Task.TaskStatus.TODO);

        Task inProgressTask = new Task();
        inProgressTask.setStatus(Task.TaskStatus.IN_PROGRESS);

        Task doneTask = new Task();
        doneTask.setStatus(Task.TaskStatus.DONE);

        when(taskRepository.findAll())
            .thenReturn(Arrays.asList(todoTask1, todoTask2, inProgressTask, doneTask));

        Map<String, Long> counts = taskService.getTaskCountByStatus();

        assertEquals(2L, counts.get("TODO"));
        assertEquals(1L, counts.get("IN_PROGRESS"));
        assertEquals(1L, counts.get("DONE"));
    }

    // AC11: getTaskCountByPriority returns correct counts per priority
    @Test
    void getTaskCountByPriority_shouldReturnCorrectCountsPerPriority() {
        Task highTask = new Task();
        highTask.setPriority(Task.Priority.HIGH);

        Task mediumTask1 = new Task();
        mediumTask1.setPriority(Task.Priority.MEDIUM);

        Task mediumTask2 = new Task();
        mediumTask2.setPriority(Task.Priority.MEDIUM);

        Task lowTask = new Task();
        lowTask.setPriority(Task.Priority.LOW);

        when(taskRepository.findAll())
            .thenReturn(Arrays.asList(highTask, mediumTask1, mediumTask2, lowTask));

        Map<String, Long> counts = taskService.getTaskCountByPriority();

        assertEquals(1L, counts.get("HIGH"));
        assertEquals(2L, counts.get("MEDIUM"));
        assertEquals(1L, counts.get("LOW"));
    }
}