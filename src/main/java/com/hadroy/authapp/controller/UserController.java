package com.hadroy.authapp.controller;

import com.hadroy.authapp.entity.User;
import com.hadroy.authapp.model.request.SignupRequest;
import com.hadroy.authapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        return new ResponseEntity<>(userService.getUser(username), HttpStatus.OK);
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<User> updateUser(@PathVariable String username,@Valid @RequestBody SignupRequest user) {
        return new ResponseEntity<>(userService.updateUser(username, user), HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        return new ResponseEntity<>(userService.deleteUser(username));
    }
}
