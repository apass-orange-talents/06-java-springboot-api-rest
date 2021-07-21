package br.com.alura.forum.controller;

import br.com.alura.forum.dto.TopicoDTO;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.modelo.Curso;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @RequestMapping("/topicos")
    @ResponseBody
    public List<TopicoDTO> lista() {
        return this.topicoRepository
                .findAll()
                .stream()
                .map(TopicoDTO::new)
                .collect(Collectors.toList());
    }
}
