package com.wb.product_manager.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Define o tempo de validade do token (ex: 1 hora).
    private static final long EXPIRE_DURATION = 60 * 60 * 1000; // 1 hora em milissegundos

    // Gera uma chave secreta segura para assinar o token.
    // Em uma aplicação real, essa chave deveria vir de um arquivo de configuração seguro.
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * Gera um token JWT para um usuário autenticado.
     * @param authentication O objeto de autenticação do Spring Security.
     * @return uma String contendo o token JWT.
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRE_DURATION);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extrai o nome de usuário (subject) de um token JWT.
     * @param token O token JWT.
     * @return O nome de usuário contido no token.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Valida um token JWT.
     * Verifica se a assinatura é válida e se o token não expirou.
     * @param token O token JWT a ser validado.
     * @return true se o token for válido, false caso contrário.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            // Se qualquer exceção ocorrer (assinatura inválida, token expirado, etc), o token é inválido.
            return false;
        }
    }
}