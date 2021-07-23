package br.com.alura.forum.controller;

import br.com.alura.forum.dto.TokenDto;
import br.com.alura.forum.form.auth.AuthForm;
import br.com.alura.forum.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDto> autentica(@RequestBody AuthForm authForm) {

        try {
            return ResponseEntity.ok(
                    new TokenDto(
                        this.tokenService.geraToken(this.authManager.authenticate(authForm.toAuthenticator())),
                        "Bearer"));
        } catch (AuthenticationException exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
