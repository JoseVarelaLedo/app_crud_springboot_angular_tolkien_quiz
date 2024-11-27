package app.repository;

import app.model.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositorio de datos para la entidad {@link Usuario}.
 *
 * <p>Proporciona acceso a las operaciones CRUD básicas y funcionalidades adicionales heredadas de {@link JpaRepository}.
 * Incluye métodos personalizados para verificar la existencia de un usuario por su nickname, buscar usuarios por rol,
 * obtener usuarios registrados después de una fecha específica y encontrar usuarios por nickname.</p>
 *
 * <p>Configuraciones específicas:
 * <ul>
 *   <li><b>@Repository:</b> Marca esta interfaz como un componente de acceso a datos en el contexto de Spring.</li>
 *   <li><b>@RepositoryRestResource:</b> Personaliza la exposición del repositorio como un recurso REST:
 *     <ul>
 *       <li><b>path:</b> Define la ruta base para los recursos relacionados con <code>Usuario</code> como <code>/usuarios</code>.</li>
 *       <li><b>collectionResourceRel:</b> Establece el nombre del enlace de colección en las respuestas JSON como <code>usuarios</code>.</li>
 *     </ul>
 *   </li>
 * </ul>
 * </p>
 *
 * <p>Ejemplo de uso REST:
 * <ul>
 *   <li><code>GET /usuarios</code>: Obtiene una lista de usuarios.</li>
 *   <li><code>POST /usuarios</code>: Crea un nuevo usuario.</li>
 *   <li><code>PUT /usuarios/{id}</code>: Actualiza un usuario existente.</li>
 *   <li><code>DELETE /usuarios/{id}</code>: Elimina un usuario por ID.</li>
 * </ul>
 * </p>
 *
 * <h3>Métodos Personalizados</h3>
 * <ul>
 *   <li><b>existsByNickname:</b> Verifica si existe un usuario con el nickname especificado.</li>
 *   <li><b>findByRolId:</b> Busca usuarios por su ID de rol, con soporte de paginación.</li>
 *   <li><b>findByNickname:</b> Busca un usuario por su nickname exacto.</li>
 *   <li><b>findByFechaRegistroAfter:</b> Obtiene usuarios registrados después de una fecha específica, con soporte de paginación.</li>
 * </ul>
 *
 * @see JpaRepository
 * @see RepositoryRestResource
 */
@Repository
@RepositoryRestResource(path = "usuarios", collectionResourceRel = "usuarios")

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByNickname(String nickname);
    Page<Usuario> findByRolId(Integer rolId, Pageable pageable);
    Optional<Usuario> findByNickname(String nickname);
    Page<Usuario> findByFechaRegistroAfter(LocalDateTime fecha, Pageable pageable);

}

