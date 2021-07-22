package br.com.alura.forum.form.topico;

import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class AtualizaTopicoForm {
    @NotBlank
    @Length(min = 5)
    private String titulo;

    @NotBlank
    @Length(min = 10)
    private String mensagem;

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }


    public Topico atualiza(Long id, TopicoRepository topicoRepository) {
        var topico = topicoRepository.getById(id);
        topico.setTitulo(this.titulo);
        topico.setMensagem(this.mensagem);

        return topicoRepository.save(topico);
    }
}
