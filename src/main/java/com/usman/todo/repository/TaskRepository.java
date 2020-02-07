package com.usman.todo.repository;

import com.usman.todo.models.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskModel, Integer> {
    public List<TaskModel> findByStatus(String status);
}
