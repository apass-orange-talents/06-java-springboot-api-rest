package br.com.alura.forum.controller;

import br.com.alura.forum.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.form.topico.AtualizaTopicoForm;
import br.com.alura.forum.form.topico.CriaTopicoForm;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoDto> lista(String nomeCurso, @PageableDefault(page = 0, size = 2, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        if(nomeCurso != null) {
            return this.topicoRepository
                    .findByCursoNome(nomeCurso, pageable)
                    .map(TopicoDto::new);
        }

        return this.topicoRepository
                .findAll(pageable)
                .map(TopicoDto::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {

        var optionalTopico = this.topicoRepository.findById(id);

        if(optionalTopico.isPresent())
            return ResponseEntity.ok().body(new DetalhesDoTopicoDto(optionalTopico.get()));

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> cadastra(@RequestBody @Valid CriaTopicoForm criaTopicoForm, UriComponentsBuilder uriBuilder) {
        var topico = this.topicoRepository.save(criaTopicoForm.toTopico(this.cursoRepository));
        return ResponseEntity.created(uriBuilder.path("/topicos/{id}")
                .buildAndExpand(topico.getId()).toUri())
                .body(new TopicoDto(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> atualiza(@PathVariable Long id, @RequestBody @Valid AtualizaTopicoForm atualizaTopicoForm) {

        var optionalTopico = this.topicoRepository.findById(id);
        if(optionalTopico.isPresent())
            return ResponseEntity.ok().body(new TopicoDto(atualizaTopicoForm.atualiza(id, this.topicoRepository)));

        return ResponseEntity.notFound().build();
    }


    @Transactional
    @DeleteMapping("/{id}")
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<?> deleta(@PathVariable  Long id) {
        var optionalTopico = this.topicoRepository.findById(id);
        if(optionalTopico.isEmpty())
            return ResponseEntity.notFound().build();

        this.topicoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
