package com.wb.product_manager;

import com.wb.product_manager.domain.Role;
import com.wb.product_manager.domain.User;
import com.wb.product_manager.repository.RoleRepository;
import com.wb.product_manager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;

@SpringBootApplication
public class ProductManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductManagerApplication.class, args);
	}
    @Bean
    CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Cria os papéis se não existirem
            Role analistaRole = roleRepository.findByName("ROLE_ANALISTA").orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ANALISTA")));
            Role supervisorRole = roleRepository.findByName("ROLE_SUPERVISOR").orElseGet(() -> roleRepository.save(new Role(null, "ROLE_SUPERVISOR")));

            // Cria um usuário ANALISTA padrão se não existir
            if (userRepository.findByUsername("analista.user").isEmpty()) {
                User analistaUser = new User();
                analistaUser.setUsername("analista.user");
                analistaUser.setPassword(passwordEncoder.encode("password123")); // Sempre codifique a senha!
                analistaUser.setRoles(new HashSet<>(Collections.singletonList(analistaRole)));
                userRepository.save(analistaUser);
            }

            // Cria um usuário SUPERVISOR padrão se não existir
            if (userRepository.findByUsername("supervisor.user").isEmpty()) {
                User supervisorUser = new User();
                supervisorUser.setUsername("supervisor.user");
                supervisorUser.setPassword(passwordEncoder.encode("superpassword456"));
                supervisorUser.setRoles(new HashSet<>(Collections.singletonList(supervisorRole)));
                userRepository.save(supervisorUser);
            }
        };
    }
}
