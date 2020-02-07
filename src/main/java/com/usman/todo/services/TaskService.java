package com.usman.todo.services;

import com.usman.todo.models.TaskModel;
import com.usman.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskModel createTask(TaskModel task) {

        TaskModel taskToBeCreated = null;
        try {
            taskToBeCreated = taskRepository.save(task);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        return taskToBeCreated;
    }

    public List<TaskModel> getAllTasks() {
        return taskRepository.findAll(Sort.by((Sort.Direction.ASC), "id"));
    }
}
