package app.repository;

import app.model.FichaUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;


//tiene que extender a JPRepository, indicando entre corchetes el tipo
//de dato con el que va a trabajar, FichaUsuario, y el tipo de dato de la PK
//que en el caso de FichaUsuario es un Long
@Repository
@RepositoryRestResource(path = "fichasUsuarios", collectionResourceRel = "fichasUsuarios")
public interface FichaUsuarioRepository extends JpaRepository<FichaUsuario, Long> {

    @Query("SELECT f FROM FichaUsuario f WHERE " +
            "LOWER(f.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(f.apellidos) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "f.telefono LIKE CONCAT('%', :filtro, '%') OR " +
            "LOWER(f.correoElectronico) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(f.direccion) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(f.nickname) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    Page<FichaUsuario> findAllByFilter(@Param("filtro") String filtro, Pageable pageable);
}
