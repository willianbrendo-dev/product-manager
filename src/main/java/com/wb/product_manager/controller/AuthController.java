package com.wb.product_manager.controller;

import com.wb.product_manager.domain.Role;
import com.wb.product_manager.domain.User;
import com.wb.product_manager.dto.AuthResponseDto;
import com.wb.product_manager.dto.LoginDto;
import com.wb.product_manager.dto.RegisterDto;
import com.wb.product_manager.repository.RoleRepository;
import com.wb.product_manager.repository.UserRepository;
import com.wb.product_manager.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        // O AuthenticationManager usa o nosso CustomUserDetailsService e PasswordEncoder
        // para verificar se as credenciais são válidas. Se não forem, ele lança uma exceção.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        // Se a autenticação for bem-sucedida, o objeto Authentication é colocado no contexto de segurança.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Geramos o token JWT para o usuário autenticado.
        String token = jwtService.generateToken(authentication);

        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        // Verifica se o nome de usuário já está em uso.
        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // Cria um novo usuário.
        User user = new User();
        user.setUsername(registerDto.getUsername());
        // Codifica a senha antes de salvar!
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Por padrão, todo novo usuário será um Analista.
        Role analistaRole = roleRepository.findByName("ROLE_ANALISTA")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setRoles(new HashSet<>(Collections.singletonList(analistaRole)));

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }
}