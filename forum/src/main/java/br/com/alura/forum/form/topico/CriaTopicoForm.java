package br.com.alura.forum.form.topico;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class CriaTopicoForm {
    @NotBlank @Length(min = 5)
    private String titulo;

    @NotBlank @Length(min = 10)
    private String mensagem;

    @NotBlank
    private String nomeCurso;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public Topico toTopico(CursoRepository cursoRepository) {
        return new Topico(this.getTitulo(), this.getMensagem(), cursoRepository.findByNome(this.getNomeCurso()));
    }
}
