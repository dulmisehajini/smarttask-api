package com.smarttask.dto;

import com.smarttask.entity.TaskPriority;
import com.smarttask.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private String projectName;
    private String assignedToName;
    private String createdByName;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
}