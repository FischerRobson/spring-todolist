package br.com.fischer.todolist.task;

import br.com.fischer.todolist.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @PostMapping()
    public ResponseEntity create(@RequestBody Task task, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        task.setUser(user);

        if (task.getStartAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start at date is before present date");
        }

        if (task.getEndAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End at date is before present date");
        }

        if (task.getEndAt().isBefore(task.getStartAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End at date is before start at date");
        }

        Task savedTask = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @GetMapping
    public ResponseEntity list(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        List<Task> tasks = this.taskRepository.findByUserId(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }
}
