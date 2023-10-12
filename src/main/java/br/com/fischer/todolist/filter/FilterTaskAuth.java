package br.com.fischer.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.fischer.todolist.user.User;
import br.com.fischer.todolist.user.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        String base64Auth = authorization.substring("Basic".length()).trim(); // beginIndex = start of susbtring
                                                                   // -> Basic.length() = 5, substring start in index 5

        byte[] decodedAuth = Base64.getDecoder().decode(base64Auth);

        String auth = new String(decodedAuth);
        String[] credentials = auth.split(":");

        String username = credentials[0];
        String password = credentials[1];

        User user = this.userRepository.findByUsername(username);

        if (user == null) {
           response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        BCrypt.Result passwordMatches = BCrypt.verifyer().verify(password.getBytes(), user.getPassword().getBytes());

        if (passwordMatches.verified) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
