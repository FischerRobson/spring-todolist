package br.com.fischer.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.status(200).body(savedTask);
    }
}
