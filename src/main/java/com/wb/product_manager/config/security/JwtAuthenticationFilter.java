package com.wb.product_manager.config.security;

import com.wb.product_manager.service.CustomUserDetailsService;
import com.wb.product_manager.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @Component: Marca esta classe para ser gerenciada pelo Spring.
 * OncePerRequestFilter: Garante que este filtro seja executado apenas UMA VEZ por requisição.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Extrair o token do cabeçalho da requisição.
        String token = getJwtFromRequest(request);

        // 2. Validar o token.
        if (StringUtils.hasText(token) && jwtService.validateToken(token)) {
            // 3. Obter o nome de usuário a partir do token.
            String username = jwtService.getUsernameFromToken(token);

            // 4. Carregar os detalhes do usuário do banco de dados.
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // 5. Criar um objeto de "autenticação" para o Spring Security.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6. Colocar o objeto de autenticação no Contexto de Segurança do Spring.
            // A partir deste ponto, o Spring considera o usuário como AUTENTICADO para esta requisição.
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 7. Passa a requisição para o próximo filtro na cadeia.
        filterChain.doFilter(request, response);
    }

    /**
     * Método auxiliar para pegar o token do cabeçalho "Authorization".
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove o prefixo "Bearer "
        }
        return null;
    }
}