package app.repository;

import app.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de datos para la entidad {@link Rol}.
 *
 * <p>Proporciona acceso a las operaciones CRUD básicas y funcionalidades adicionales heredadas de {@link JpaRepository}.
 * Incluye un método personalizado para buscar roles por nombre.</p>
 *
 * <p>Configuraciones específicas:
 * <ul>
 *   <li><b>@Repository:</b> Marca esta interfaz como un componente de acceso a datos en el contexto de Spring.</li>
 *   <li><b>@RepositoryRestResource:</b> Personaliza la exposición del repositorio como un recurso REST:
 *     <ul>
 *       <li><b>path:</b> Define la ruta base para los recursos relacionados con <code>Rol</code> como <code>/roles</code>.</li>
 *       <li><b>collectionResourceRel:</b> Establece el nombre del enlace de colección en las respuestas JSON como <code>roles</code>.</li>
 *     </ul>
 *   </li>
 * </ul>
 * </p>
 *
 * <p>Ejemplo de uso REST:
 * <ul>
 *   <li><code>GET /roles</code>: Obtiene una lista de roles.</li>
 *   <li><code>POST /roles</code>: Crea un nuevo rol.</li>
 *   <li><code>PUT /roles/{id}</code>: Actualiza un rol existente.</li>
 *   <li><code>DELETE /roles/{id}</code>: Elimina un rol por ID.</li>
 * </ul>
 * </p>
 *
 * <h3>Método Personalizado</h3>
 * <ul>
 *   <li><b>findByNombreRol:</b> Busca un rol por su nombre exacto.</li>
 * </ul>
 *
 * @see JpaRepository
 * @see RepositoryRestResource
 */
@Repository
@RepositoryRestResource(path = "roles`", collectionResourceRel = "roles")

public interface RolRepository extends JpaRepository <Rol, Integer>{
    Optional<Rol> findByNombreRol(String nombreRol);
}