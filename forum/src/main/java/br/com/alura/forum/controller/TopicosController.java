package br.com.alura.forum.controller;

import br.com.alura.forum.dto.TopicoDTO;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.modelo.Curso;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDTO> lista(String nomeCurso) {

        if(nomeCurso != null) {
            return this.topicoRepository
                    .findByCursoNome(nomeCurso)
                    .stream()
                    .map(TopicoDTO::new)
                    .collect(Collectors.toList());
        }

        return this.topicoRepository
                .findAll()
                .stream()
                .map(TopicoDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<TopicoDTO> cadastra(@RequestBody TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
        var topico = this.topicoRepository.save(topicoForm.toTopico(this.cursoRepository));
        return ResponseEntity.created(uriBuilder.path("/topicos/{id}")
                .buildAndExpand(topico.getId()).toUri())
                .body(new TopicoDTO(topico));
    }
}
