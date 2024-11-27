package app.repository;

import app.model.FichaUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de datos para la entidad {@link FichaUsuario}.
 *
 * <p>Proporciona acceso a las operaciones CRUD básicas y funcionalidades adicionales
 * heredadas de {@link JpaRepository}. Además, incluye un método personalizado para
 * realizar búsquedas dinámicas en los campos principales de la entidad.</p>
 *
 * <p>Configuraciones específicas:
 * <ul>
 *   <li><b>@Repository:</b> Marca esta interfaz como un componente de acceso a datos en el contexto de Spring.</li>
 *   <li><b>@RepositoryRestResource:</b> Personaliza la exposición del repositorio como un recurso REST:
 *     <ul>
 *       <li><b>path:</b> Define la ruta base para los recursos relacionados con <code>FichaUsuario</code> como <code>/fichasUsuarios</code>.</li>
 *       <li><b>collectionResourceRel:</b> Establece el nombre del enlace de colección en las respuestas JSON como <code>fichasUsuarios</code>.</li>
 *     </ul>
 *   </li>
 * </ul>
 * </p>
 *
 * <p>Ejemplo de uso REST:
 * <ul>
 *   <li><code>GET /fichasUsuarios</code>: Obtiene una lista de fichas de usuario.</li>
 *   <li><code>POST /fichasUsuarios</code>: Crea una nueva ficha de usuario.</li>
 *   <li><code>PUT /fichasUsuarios/{id}</code>: Actualiza una ficha de usuario existente.</li>
 *   <li><code>DELETE /fichasUsuarios/{id}</code>: Elimina una ficha de usuario por ID.</li>
 * </ul>
 * </p>
 *
 * <h3>Métodos Personalizados</h3>
 * <ul>
 *   <li><b>findAllByFilter:</b> Realiza una búsqueda dinámica en los campos principales de la entidad
 *   (nombre, apellidos, teléfono, correo electrónico, dirección y nickname). Ideal para implementaciones de
 *   buscadores o filtros avanzados.</li>
 * </ul>
 *
 * @see JpaRepository
 * @see RepositoryRestResource
 */
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
