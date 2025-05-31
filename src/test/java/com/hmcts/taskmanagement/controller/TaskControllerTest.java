package com.hmcts.taskmanagement.controller;

import com.hmcts.taskmanagement.model.Task;
import com.hmcts.taskmanagement.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    private TaskService taskService;
    private TaskController taskController;
    LocalDateTime now;

    @BeforeEach
    void setUp() {
        taskService=mock(TaskService.class);
        taskController=new TaskController(taskService);
        now=LocalDateTime.now();
    }

    @Test
    void testGetAllTasks() {
        Task task1=new Task(1L,"Task 1","Desc 1","Pending",now );
        Task task2=new Task(2L,"Task 2","Desc 2","In Progress",now);
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1,task2));
        ResponseEntity<List<Task>> response=taskController.getAllTasks();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        List<Task> tasks=response.getBody();
        assertNotNull(tasks);
        assertEquals(2,tasks.size());
        verify(taskService).getAllTasks();
;    }

    @Test
    void getTaskById() {
        Task task=new Task(1L,"Test Task","Test Desc","Completed",now);
        when(taskService.getTaskById(1l)).thenReturn(task);
        ResponseEntity<Task> response=taskController.getTaskById(1L);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        Task responseTask=response.getBody();
        assertNotNull(responseTask);
        assertEquals("Test Task",responseTask.getTitle());
        verify(taskService).getTaskById(1L);

    }

    @Test
    void createTask() {
        Task newTask=new Task(null,"New Task","New Desc","Pending",now);
        Task createdTask=new Task(1L,"New Task","New Desc","Pending",now);

        when(taskService.createTask(newTask)).thenReturn(createdTask);
        ResponseEntity<Task> response=taskController.createTask(newTask);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdTask.getTitle(),response.getBody().getTitle());

    }

    @Test
    void updateTask() {
        Task updatedTask=new Task(1L,"Updated Task","Updated Desc","Completed",now);
        when(taskService.updateTask(eq(1L),any(Task.class))).thenReturn(updatedTask);
        ResponseEntity<Task> response=taskController.updateTask(1L,updatedTask);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Task",response.getBody().getTitle());
        assertEquals("Completed",response.getBody().getStatus());
    }

    @Test
    void deleteTask() {
        Long taskId = 1L;
        doNothing().when(taskService).deleteTask(taskId);
        ResponseEntity<Void> response = taskController.deleteTask(taskId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());  // HTTP 204 No Content
        verify(taskService, times(1)).deleteTask(taskId);
    }
}