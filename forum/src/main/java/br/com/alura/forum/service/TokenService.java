package br.com.alura.forum.service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.Token;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private Long expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String geraToken(Authentication authentication) {
        var at = new Date();

        var exp = new Date();
        exp.setTime(at.getTime() + this.expiration);

        return Jwts.builder()
                .setIssuer("API do Forum da Alura")
                .setSubject(((Usuario) authentication.getPrincipal()).getId().toString())
                .setIssuedAt(at)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256,  this.secret)
                .compact();
    }

    /**
     *
     * @param token
     * @return
     */
    public Long getIdUsuario(String token) {
       return Long.parseLong(Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody().getSubject());
    }
}
