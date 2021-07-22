package br.com.alura.forum.controller;

import br.com.alura.forum.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.form.topico.AtualizaTopicoForm;
import br.com.alura.forum.form.topico.CriaTopicoForm;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
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

    @GetMapping("/{id}")
    public DetalhesDoTopicoDto detalhar(@PathVariable Long id) {
        return new DetalhesDoTopicoDto(this.topicoRepository.getById(id));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<TopicoDto> cadastra(@RequestBody @Valid CriaTopicoForm criaTopicoForm, UriComponentsBuilder uriBuilder) {
        var topico = this.topicoRepository.save(criaTopicoForm.toTopico(this.cursoRepository));
        return ResponseEntity.created(uriBuilder.path("/topicos/{id}")
                .buildAndExpand(topico.getId()).toUri())
                .body(new TopicoDto(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualiza(@PathVariable Long id, @RequestBody @Valid AtualizaTopicoForm atualizaTopicoForm) {
        return ResponseEntity.ok().body(
                new TopicoDto(atualizaTopicoForm.atualiza(id, this.topicoRepository))
        );
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleta(@PathVariable  Long id) {
        this.topicoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
