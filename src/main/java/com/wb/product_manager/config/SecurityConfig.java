package com.wb.product_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

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
public class SecurityConfig {

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
                /**
                 * CSRF (Cross-Site Request Forgery) é um tipo de ataque.
                 * Como nossa API será stateless (não usará sessões) e a autenticação
                 * será via token, podemos desabilitar essa proteção com segurança.
                 */
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                /**
                 * authorizeHttpRequests: É aqui que definimos as regras de autorização.
                 * .requestMatchers("/api/**"): Estamos especificando o padrão de URL.
                 * O "/api/**" significa: "qualquer URL que comece com /api/".
                 * .permitAll(): Estamos dizendo que para as URLs que correspondem ao
                 * padrão acima, o acesso é permitido para todos, sem necessidade
                 * de autenticação.
                 */
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated() // Para qualquer outra requisição, exija autenticação (voltaremos nisso)
                );

        return http.build();
    }
}