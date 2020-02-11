package com.usman.todo.services;

import com.usman.todo.models.TaskModel;
import com.usman.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void createTask(TaskModel task) {
        task.setStatus("pending");
        taskRepository.save(task);
    }

    public List<TaskModel> getAllTasks() {
        return taskRepository.findAll(Sort.by((Sort.Direction.ASC), "id"));
    }

    public TaskModel getATask(Integer id) {
        Optional<TaskModel> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            return null;
        }
        return task.get();
    }

    public List<TaskModel> getTasksByStatus(String status) {
        List<TaskModel> tasks = taskRepository.findByStatus(status);
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks;
    }

    public TaskModel editTask(Integer id, TaskModel task) {
        TaskModel existingTask = taskRepository.findById(id).orElse(null);
        if (existingTask != null) {
            existingTask.setDescription(task.getDescription());
            existingTask.setTitle(task.getTitle());
            existingTask.setStatus(task.getStatus());
            existingTask = taskRepository.save(existingTask);
            if (existingTask.getStatus().equalsIgnoreCase("done")) {
                existingTask.setCompletedAt(task.getUpdatedAt());
            }
            return existingTask;
        }
        return null;
    }

    public void deleteATask(Integer id) {
        taskRepository.deleteById(id);
    }
}
