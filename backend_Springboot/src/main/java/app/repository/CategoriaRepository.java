package app.repository;

import app.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "categorias", collectionResourceRel = "categorias")

public interface CategoriaRepository extends JpaRepository <Categoria, Long>{
}