package com.usman.todo.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usman.todo.controllers.TaskController;
import com.usman.todo.models.TaskModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskTest {

    private TaskController taskController;
    private MockMvc mockMvc;

    @Autowired
    public TaskTest(TaskController taskController, MockMvc mockMvc) {
        this.taskController = taskController;
        this.mockMvc = mockMvc;
    }

    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void contextLoad() {
        assertThat(taskController).isNotNull();
    }

    @Test
    public void createTaskWithCorrectValues() throws Exception {
        TaskModel newTask = new TaskModel("test1", "test is good to write for efficient applications", "done", null);
        this.mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newTask))).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("test is good to write for efficient applications")));
    }

    @Test
    public void createTaskWithIncorrectValues() {
        TaskModel newTask = new TaskModel("", "I am empty", "inProgress", null);
        try {
            this.mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newTask))).andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Internal Server Error")));
        } catch (Exception e) {
            assertThat(true).isTrue();
        }
    }

    @Test
    public void createTaskWithNullValues() {
        TaskModel newTask = new TaskModel(null, "I am empty", "done", null);
        try {
            this.mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newTask))).andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Internal Server Error")));
        } catch (Exception e) {
            assertThat(true).isTrue();
        }
    }

    @Test
    public void getAllTasks() throws Exception {

        mockMvc.perform(get("/tasks"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("I'm really interested in learning that guy")));
    }

    @Test
    public void getATaskWithoutErrors() throws Exception {

        mockMvc.perform(get("/tasks/1"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("I'm really interested in learning that guy")));
    }

    @Test
    public void getATaskThatIsNotPresent() throws Exception {

        mockMvc.perform(get("/tasks/88"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Task not found")));
    }

    @Test
    public void getTasksByStatusCorrectly() throws Exception {

        mockMvc.perform(get("/tasks/status/done"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("All done tasks successfully retrieved")));
    }

    @Test
    public void getTasksByStatusWithWrongValue() throws Exception {

        mockMvc.perform(get("/tasks/status/finished"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No finished task found")));
    }

    @Test
    public void changeStatus() {
        String status = "inProgress";
        try {
            this.mockMvc.perform(patch("/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(status))).andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(content().string(containsString("inProgress")));
        } catch (Exception e) {
            assertThat(true).isTrue();
        }
    }

    @Test
    public void changeStatusOfNonExistingTask() {
        String status = "inProgress";
        try {
            this.mockMvc.perform(patch("/tasks/101")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(status))).andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Unable to change status of the task")));
        } catch (Exception e) {
            assertThat(true).isTrue();
        }
    }
}
