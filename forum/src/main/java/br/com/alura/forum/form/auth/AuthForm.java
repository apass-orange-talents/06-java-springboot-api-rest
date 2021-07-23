package br.com.alura.forum.form.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthForm {

    private String email;

    private String senha;

    public AuthForm(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UsernamePasswordAuthenticationToken toAuthenticator() {
        return new UsernamePasswordAuthenticationToken(this.email, this.senha);
    }
}
