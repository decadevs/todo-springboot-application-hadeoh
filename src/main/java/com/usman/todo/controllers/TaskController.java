package com.usman.todo.controllers;

import com.usman.todo.models.TaskModel;
import com.usman.todo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/add")
    public String showCreateTask(Model model) {
        model.addAttribute("create", true);
        model.addAttribute("task", new TaskModel());
        return "add";
    }

    @PostMapping("/create")
    public String createTask(TaskModel task) {
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    @GetMapping
    public String getAllTasks(Model model) {
        List<TaskModel> allTasks = taskService.getAllTasks();
        model.addAttribute("tasks", allTasks);
        return "task";
    }

    @GetMapping("{id}")
    public String getATask(Model model, @PathVariable Integer id) {
        TaskModel task = taskService.getATask(id);
        model.addAttribute("oneTask", task);
        return "view-task";
    }

    @GetMapping("/status/{status}")
    public MyResponse<List<TaskModel>> getTasksByStatus(@PathVariable String status, HttpServletResponse response) {
        List<TaskModel> tasks = taskService.getTasksByStatus(status);
        String message = String.format("All %s tasks successfully retrieved", status);
        HttpStatus statusCode = HttpStatus.OK;
        if (tasks == null) {
            message = String.format("No %s task found", status);
            statusCode = HttpStatus.NOT_FOUND;
        }
        response.setStatus(statusCode.value());
        return new MyResponse<>(statusCode, message, tasks);
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String getEditTask(@PathVariable Integer id, Model model) {
        model.addAttribute("editTask", taskService.getATask(id));
        return "edit";
    }

    @RequestMapping(value = "/edit/task/{id}", method = RequestMethod.POST)
    public String editTask(@PathVariable Integer id, TaskModel task) {
        taskService.editTask(id, task);
        return "redirect:/tasks";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteATask(@PathVariable Integer id) {
        taskService.deleteATask(id);
        return "redirect:/tasks";
    }
}
