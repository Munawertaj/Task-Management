package com.bs23.taskmanagement.controller;

import com.bs23.taskmanagement.dto.TaskDTO;
import com.bs23.taskmanagement.model.Task;
import com.bs23.taskmanagement.model.TaskStatus;
import com.bs23.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user/tasks")
@PreAuthorize("hasRole('USER')")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "deadline") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            Model model) {

        Page<Task> taskPage = taskService.getCurrentUserTasks(page, size, sortField, sortDirection, status, search);

        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("totalItems", taskPage.getTotalElements());
        model.addAttribute("status", status);
        model.addAttribute("search", search);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("statuses", TaskStatus.values());

        return "user/tasks/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new TaskDTO());
        model.addAttribute("statuses", TaskStatus.values());
        return "user/tasks/create";
    }

    @PostMapping("/new")
    public String createTask(@Valid @ModelAttribute("task") TaskDTO taskDTO,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", TaskStatus.values());
            return "user/tasks/create";
        }

        taskService.createTask(taskDTO);
        return "redirect:/user/tasks?success=Task created successfully";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskByIdAndCurrentUser(id);

        TaskDTO taskDTO = TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .status(task.getStatus())
                .build();

        model.addAttribute("task", taskDTO);
        model.addAttribute("statuses", TaskStatus.values());
        return "user/tasks/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateTask(@PathVariable Long id,
                             @Valid @ModelAttribute("task") TaskDTO taskDTO,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", TaskStatus.values());
            return "user/tasks/edit";
        }

        taskService.updateTask(id, taskDTO);
        return "redirect:/user/tasks?success=Task updated successfully";
    }

    @GetMapping("/{id}/details")
    public String showTaskDetails(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskByIdAndCurrentUser(id);
        model.addAttribute("task", task);
        return "user/tasks/details";
    }

    @GetMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskService.deleteTask(id);
        redirectAttributes.addFlashAttribute("success", "Task deleted successfully");
        return "redirect:/user/tasks";
    }

    @GetMapping("/{id}/toggle-status")
    public String toggleTaskStatus(@PathVariable Long id) {
        taskService.toggleTaskStatus(id);
        return "redirect:/user/tasks";
    }
}
