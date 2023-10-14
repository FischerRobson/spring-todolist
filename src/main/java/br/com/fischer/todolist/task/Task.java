package br.com.fischer.todolist.task;

import br.com.fischer.todolist.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @Column(length = 50)
    private String title;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    public void setTitle(String title) throws Exception {
        if (title.length() > 50) {
            throw new Exception("Title must contain less than 50 characters");
        }
        this.title = title;
    }

}
