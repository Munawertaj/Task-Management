package com.bs23.taskmanagement.dto;

import com.bs23.taskmanagement.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    @NotNull(message = "Status is required")
    private TaskStatus status;
}
