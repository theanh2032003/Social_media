package com.example.demo;

import com.example.demo.model.ERole;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void run(String... args) throws Exception {
//		roleRepository.save(new Role(ERole.ROLE_USER));
//		roleRepository.save(new Role(ERole.ROLE_ADMIN));
	}
}
