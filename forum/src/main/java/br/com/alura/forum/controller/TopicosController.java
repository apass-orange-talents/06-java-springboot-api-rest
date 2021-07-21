package br.com.alura.forum.controller;

import br.com.alura.forum.dto.TopicoDTO;
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

    @RequestMapping("/topicos")
    @ResponseBody
    public List<TopicoDTO> lista() {
        Topico topico = new Topico("Dúvida", "Dúvida com Spring", new Curso("Spring", "Programação"));

        return Arrays.asList(topico, topico, topico).stream().map(TopicoDTO::new).collect(Collectors.toList());
    }
}
