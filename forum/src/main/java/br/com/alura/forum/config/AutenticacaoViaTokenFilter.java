package br.com.alura.forum.config;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import br.com.alura.forum.service.TokenService;
import io.jsonwebtoken.JwtException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    private UsuarioRepository usuariorepository;

    public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuariorepository) {
        this.usuariorepository = usuariorepository;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = this.recuperaToken(request);
        if (token != null) {
            try {
                Usuario usuario = this.usuariorepository.findById(this.tokenService.getIdUsuario(token)).get();
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(
                                usuario.getUsername(), null, usuario.getAuthorities()));

                filterChain.doFilter(request, response);

            } catch (JwtException jwtException) {
                ((HttpServletResponse) response).sendError(HttpStatus.BAD_REQUEST.value(), "Token inválido!");
            }
        }

    }

    private String recuperaToken(HttpServletRequest request) {
        var hAuth = request.getHeader("Authorization");
        if(hAuth == null || hAuth.isEmpty() || !hAuth.startsWith("Bearer ")) {
            return null;
        }
        return hAuth.substring(7, hAuth.length());
    }
}
