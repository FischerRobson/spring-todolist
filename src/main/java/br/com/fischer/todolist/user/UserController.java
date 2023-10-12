package br.com.fischer.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping()
    public ResponseEntity create(@RequestBody User user) {
        User userWithUsername = this.userRepository.findByUsername(user.getUsername());
        if (userWithUsername != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        String passwordHashed = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(passwordHashed);

        User savedUser = this.userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

}
