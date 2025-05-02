package com.bs23.taskmanagement.service;

import com.bs23.taskmanagement.dto.TaskDTO;
import com.bs23.taskmanagement.model.Task;
import com.bs23.taskmanagement.model.TaskStatus;
import com.bs23.taskmanagement.model.User;
import com.bs23.taskmanagement.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Transactional
    public Task createTask(TaskDTO taskDTO) {
        User currentUser = userService.getCurrentUser();

        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .deadline(taskDTO.getDeadline())
                .status(taskDTO.getStatus())
                .user(currentUser)
                .build();

        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Long taskId, TaskDTO taskDTO) {
        Task task = getTaskByIdAndCurrentUser(taskId);

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDeadline(taskDTO.getDeadline());
        task.setStatus(taskDTO.getStatus());

        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        Task task = getTaskByIdAndCurrentUser(taskId);
        taskRepository.delete(task);
    }

    @Transactional
    public Task toggleTaskStatus(Long taskId) {
        Task task = getTaskByIdAndCurrentUser(taskId);

        TaskStatus newStatus;
        switch (task.getStatus()) {
            case PENDING -> newStatus = TaskStatus.IN_PROGRESS;
            case IN_PROGRESS -> newStatus = TaskStatus.COMPLETED;
            case COMPLETED -> newStatus = TaskStatus.PENDING;
            default -> newStatus = TaskStatus.PENDING;
        }

        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
    }

    @Transactional(readOnly = true)
    public Task getTaskByIdAndCurrentUser(Long taskId) {
        Task task = getTaskById(taskId);
        User currentUser = userService.getCurrentUser();

        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to access this task");
        }

        return task;
    }

    @Transactional(readOnly = true)
    public Page<Task> getCurrentUserTasks(int page, int size, String sortField, String sortDirection, TaskStatus status, String search) {
        User currentUser = userService.getCurrentUser();

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        if (status != null && search != null && !search.trim().isEmpty()) {
            return taskRepository.findByUserAndStatusAndTitleContaining(currentUser, status, search, pageable);
        } else if (status != null) {
            return taskRepository.findByUserAndStatus(currentUser, status, pageable);
        } else if (search != null && !search.trim().isEmpty()) {
            return taskRepository.findByUserAndTitleContaining(currentUser, search, pageable);
        } else {
            return taskRepository.findByUser(currentUser, pageable);
        }
    }
}
