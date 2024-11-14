package app.repository;

import app.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(path = "roles`", collectionResourceRel = "roles")

public interface RolRepository extends JpaRepository <Rol, Integer>{
    Optional<Rol> findByNombreRol(String nombreRol);
}