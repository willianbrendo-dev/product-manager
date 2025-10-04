package com.wb.product_manager.config;

import com.wb.product_manager.config.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Configuration: Indica ao Spring que esta é uma classe de configuração.
 * O Spring irá carregar e processar as definições de Beans contidas aqui
 * durante a inicialização da aplicação.
 *
 * @EnableWebSecurity: Habilita a integração do Spring Security com o Spring MVC.
 * É essencial para que nossa configuração de segurança seja aplicada.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * @Bean: Esta anotação diz ao Spring: "Aqui está a receita para criar um objeto
     * (neste caso, um SecurityFilterChain). Crie-o e gerencie-o para mim."
     *
     * SecurityFilterChain: É o objeto que o Spring Security usa para determinar
     * qual cadeia de filtros de segurança aplicar a uma determinada requisição.
     * É aqui que configuramos quais endpoints são públicos, quais são privados,
     * desabilitamos recursos, etc.
     *
     * @param http o objeto HttpSecurity que usamos para construir nossa configuração.
     * @return a cadeia de filtros de segurança configurada.
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                    // Nossos endpoints de autenticação são públicos
                    .requestMatchers("/api/auth/**").permitAll()
                    // Todos os outros endpoints exigem autenticação
                    .anyRequest().authenticated()
            );
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}