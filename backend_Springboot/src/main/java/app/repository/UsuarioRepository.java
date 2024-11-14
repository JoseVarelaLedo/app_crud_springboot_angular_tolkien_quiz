package app.repository;


import app.model.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
@RepositoryRestResource(path = "usuarios", collectionResourceRel = "usuarios")

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByNickname(String nickname);
    Page<Usuario> findByRolId(Integer rolId, Pageable pageable);
    Optional<Usuario> findByNickname(String nickname);
    Page<Usuario> findByFechaRegistroAfter(LocalDateTime fecha, Pageable pageable);

}

