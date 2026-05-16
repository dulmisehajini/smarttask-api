package com.smarttask.service;

import com.smarttask.dto.TaskRequest;
import com.smarttask.dto.TaskResponse;
import com.smarttask.entity.Task;
import com.smarttask.entity.TaskStatus;
import com.smarttask.entity.User;
import com.smarttask.repository.ProjectRepository;
import com.smarttask.repository.TaskRepository;
import com.smarttask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // Get currently logged in user
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Create task
    public TaskResponse createTask(TaskRequest request) {
        var project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        var currentUser = getCurrentUser();

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .project(project)
                .createdBy(currentUser)
                .dueDate(request.getDueDate())
                .build();

        if (request.getAssignedToId() != null) {
            var assignedTo = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedTo(assignedTo);
        }

        return mapToResponse(taskRepository.save(task));
    }

    // Get all tasks
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get task by id
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return mapToResponse(task);
    }

    // Update task
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        var project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setProject(project);
        task.setDueDate(request.getDueDate());

        if (request.getAssignedToId() != null) {
            var assignedTo = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedTo(assignedTo);
        }

        return mapToResponse(taskRepository.save(task));
    }

    // Update task status
    public TaskResponse updateTaskStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        return mapToResponse(taskRepository.save(task));
    }

    // Delete task
    public void deleteTask(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.deleteById(id);
    }

    // Map Task entity to TaskResponse DTO
    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .projectName(task.getProject() != null ? task.getProject().getName() : null)
                .assignedToName(task.getAssignedTo() != null ? task.getAssignedTo().getName() : null)
                .createdByName(task.getCreatedBy() != null ? task.getCreatedBy().getName() : null)
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .build();
    }
}