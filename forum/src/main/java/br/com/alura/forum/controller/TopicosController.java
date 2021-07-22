package br.com.alura.forum.controller;

import br.com.alura.forum.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import br.com.alura.forum.modelo.Topico;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/topicos")
@ResponseBody
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDto> lista(String nomeCurso) {

        if(nomeCurso != null) {
            return this.topicoRepository
                    .findByCursoNome(nomeCurso)
                    .stream()
                    .map(TopicoDto::new)
                    .collect(Collectors.toList());
        }

        return this.topicoRepository
                .findAll()
                .stream()
                .map(TopicoDto::new)
                .collect(Collectors.toList());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String NotFoundExceptionHandler(NotFoundException exception) {
        return exception.getMessage();
    }

    @GetMapping("/{id}")
    public DetalhesDoTopicoDto detalhar(@PathVariable Long id) {
        return new DetalhesDoTopicoDto(this.topicoRepository.getById(id));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<TopicoDto> cadastra(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
        var topico = this.topicoRepository.save(topicoForm.toTopico(this.cursoRepository));
        return ResponseEntity.created(uriBuilder.path("/topicos/{id}")
                .buildAndExpand(topico.getId()).toUri())
                .body(new TopicoDto(topico));
    }
}
