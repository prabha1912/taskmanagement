package com.hmcts.taskmanagement.service;

import com.hmcts.taskmanagement.model.Task;
import com.hmcts.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id)
    {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.orElse(null);
    }
    //update task status
    public Task updateTask(Long id, Task updatedTask)
    {
            Task existingTask = getTaskById(id);
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setStatus(updatedTask.getStatus());
            existingTask.setDueDateTime(updatedTask.getDueDateTime());
            return taskRepository.save(existingTask);
    }
    public void deleteTask(Long id)
    {
        Optional<Task> optionalTask=taskRepository.findById(id);
        if(optionalTask.isPresent()) {
            taskRepository.deleteById(id);
        }
    }
}
