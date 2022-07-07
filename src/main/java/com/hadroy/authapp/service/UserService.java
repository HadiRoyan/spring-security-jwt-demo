package com.hadroy.authapp.service;

import com.hadroy.authapp.entity.Role;
import com.hadroy.authapp.entity.User;
import com.hadroy.authapp.error.NotFoundException;
import com.hadroy.authapp.error.UserRegisterException;
import com.hadroy.authapp.model.request.SignupRequest;
import com.hadroy.authapp.repository.RoleRepository;
import com.hadroy.authapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.hadroy.authapp.entity.RoleApp.ROLE_ADMIN;
import static com.hadroy.authapp.entity.RoleApp.ROLE_USER;

@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public User saveUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UserRegisterException("username has been used");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UserRegisterException("email has been used");
        }

        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );
        Collection<Role> saveRoles = getRoles(signupRequest.getRole());

        user.setRoles(saveRoles);
        log.info("Register {}", user.toString());
        userRepository.save(user);

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("username {} is not found in database", username);
                    throw new UsernameNotFoundException("username is not found");
                });
    }

    public User updateUser(String username, SignupRequest user) {
        User updateDataUser = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("username {} is not found in database", username);
                    throw new UsernameNotFoundException("username is not found");
                });

        updateDataUser.setUsername(user.getUsername());
        updateDataUser.setEmail(user.getEmail());
        updateDataUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updateDataUser.setRoles(getRoles(user.getRole()));

        userRepository.save(updateDataUser);

        return updateDataUser;
    }

    public HttpStatus deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("username {} is not found in database", username);
                    throw new UsernameNotFoundException("username is not found");
                });

        userRepository.delete(user);
        return HttpStatus.OK;
    }

    private Collection<Role> getRoles(Collection<String> roles) {
        Collection<Role> rolesUser = new ArrayList<>();

        if (roles.size() == 0) {
            Role userRole = roleRepository.findByName(ROLE_USER)
                    .orElseThrow(() -> new NotFoundException("Error: role is not found"));
            rolesUser.add(userRole);
        } else {
            roles.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleRepository.findByName(ROLE_ADMIN)
                            .orElseThrow(() -> new NotFoundException("Error: role is not found"));
                    rolesUser.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ROLE_USER)
                            .orElseThrow(() -> new NotFoundException("Error: role is not found"));
                    rolesUser.add(userRole);
                }
            });
        }

        return rolesUser;
    }


}
