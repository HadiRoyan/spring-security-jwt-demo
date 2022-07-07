package com.hadroy.authapp;

import com.hadroy.authapp.entity.Role;
import com.hadroy.authapp.repository.RoleRepository;
import com.hadroy.authapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.hadroy.authapp.entity.RoleApp.ROLE_ADMIN;
import static com.hadroy.authapp.entity.RoleApp.ROLE_USER;

@SpringBootApplication
public class AuthAppDemoApplication {

	@Autowired
	public RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(AuthAppDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			roleRepository.save(new Role(ROLE_USER));
			roleRepository.save(new Role(ROLE_ADMIN));
		};
	}
}
