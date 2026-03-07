package com.taskmanager.taskmanager.service;

import com.taskmanager.taskmanager.model.Task;
import com.taskmanager.taskmanager.model.TaskSummary;
import com.taskmanager.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existing = getTaskById(id);
        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setStatus(updatedTask.getStatus());
        existing.setDueDate(updatedTask.getDueDate());
        existing.setPriority(updatedTask.getPriority());
        return taskRepository.save(existing);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksByStatus(Task.TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getOverdueTasks() {
        return taskRepository.findByDueDateBeforeAndStatusNot(
                LocalDate.now(), Task.TaskStatus.DONE);
    }

    public Map<String, Long> getTaskCountByStatus() {
        return taskRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        t -> t.getStatus().name(), Collectors.counting()));
    }

    public Map<String, Long> getTaskCountByPriority() {
        return taskRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        t -> t.getPriority().name(), Collectors.counting()));
    }

    public TaskSummary getTaskSummary() {
        long total = taskRepository.count();
        Map<String, Long> byStatus = getTaskCountByStatus();
        Map<String, Long> byPriority = getTaskCountByPriority();
        long overdue = getOverdueTasks().size();
        return new TaskSummary(total, byStatus, byPriority, overdue);
    }
}