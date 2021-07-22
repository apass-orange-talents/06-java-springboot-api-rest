package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface CursoRepository extends Repository<Curso, Long> {
    Curso findByNome(String nomeCurso);
}
