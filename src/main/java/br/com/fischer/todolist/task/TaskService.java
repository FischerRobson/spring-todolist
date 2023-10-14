package br.com.fischer.todolist.task;

import br.com.fischer.todolist.user.User;
import br.com.fischer.todolist.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    public ResponseEntity createTask(Task task, User user) {
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

    public ResponseEntity listTasksByUser(UUID userId) {
        List<Task> tasks = this.taskRepository.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    public ResponseEntity updateTask(Task task, UUID taskId, User user) {
        Optional<Task> previousTask = this.taskRepository.findById(taskId);

        if (previousTask.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        if (previousTask.get().getUser().getId().equals(user.getId())) {
            Utils.copyNonNullProperties(task, previousTask);

            Task savedTask = this.taskRepository.save(task);
            return ResponseEntity.status(HttpStatus.OK).body(savedTask);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This task not belong to current user");
        }
    }

}
