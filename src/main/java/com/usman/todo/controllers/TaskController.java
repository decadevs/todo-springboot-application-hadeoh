package com.usman.todo.controllers;

import com.usman.todo.models.TaskModel;
import com.usman.todo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public MyResponse<TaskModel> createTask(@RequestBody TaskModel task, HttpServletResponse response) {

        TaskModel taskToBeCreated = taskService.createTask(task);
        HttpStatus statusCode = HttpStatus.CREATED;
        String message = "Task successfully created";
        if (taskToBeCreated == null) {
            statusCode = HttpStatus.UNPROCESSABLE_ENTITY;
            message = "You just made a bad request";
        }
        response.setStatus(statusCode.value());
        return new MyResponse<>(statusCode, message, taskToBeCreated);
    }

    @GetMapping
    public MyResponse<List<TaskModel>> getAllTasks(HttpServletResponse response) {
        List<TaskModel> allTasks = taskService.getAllTasks();
        HttpStatus statusCode = HttpStatus.OK;
        String message = "All tasks successfully retrieved";
        if (allTasks.size() == 0) {
            statusCode = HttpStatus.NOT_FOUND;
            message = "There are no tasks available";
        }
        response.setStatus((statusCode.value()));
        return new MyResponse<>(statusCode, message, allTasks);
    }

    @GetMapping
    @RequestMapping("{id}")
    public MyResponse<TaskModel> getATask(@PathVariable Integer id, HttpServletResponse response) {
        TaskModel task = taskService.getATask(id);
        String message = "Task successfully retrieved";
        HttpStatus statusCode = HttpStatus.OK;
        if (task == null) {
            message = "Task not found";
            statusCode = HttpStatus.NOT_FOUND;
        }
        response.setStatus(statusCode.value());
        return new MyResponse<>(statusCode, message, task);
    }
}
