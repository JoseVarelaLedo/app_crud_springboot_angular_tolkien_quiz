package app.repository;

import app.model.Pregunta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;


@Repository
@RepositoryRestResource(path = "preguntas`", collectionResourceRel = "preguntas")

public interface PreguntaRepository extends JpaRepository <Pregunta, Long>{
    Page<Pregunta> findByCategoriaId(Long categoriaId, Pageable pageable);
    Page<Pregunta> findByTextoPreguntaContainingIgnoreCase(String textoPregunta, Pageable pageable);
    @Query("SELECT p FROM Pregunta p WHERE (:categoriaId IS NULL OR p.categoria.id = :categoriaId)")
    Page <Pregunta> findQuestionByCategoryId (@Param("categoriaId")Integer categoriaId, Pageable pageable);
}

