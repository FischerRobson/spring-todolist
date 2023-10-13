package br.com.fischer.todolist.task;

import br.com.fischer.todolist.user.User;
import br.com.fischer.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping()
    public ResponseEntity create(@RequestBody Task task, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return this.taskService.createTask(task, user);
    }

    @GetMapping
    public ResponseEntity list(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return this.taskService.listTasksByUser(user.getId());
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@RequestBody Task task, HttpServletRequest request, @PathVariable UUID id) {
        User user = (User) request.getAttribute("user");
        return this.taskService.updateTask(task, id, user);
    }
}
