package app.repository;

import app.model.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "respuestas", collectionResourceRel = "respuestas")

public interface RespuestaRepository extends JpaRepository <Respuesta, Long>{
}