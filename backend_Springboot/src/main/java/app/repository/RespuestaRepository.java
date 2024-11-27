package app.repository;

import app.model.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de datos para la entidad {@link Respuesta}.
 *
 * <p>Proporciona acceso a las operaciones CRUD básicas y funcionalidades adicionales heredadas de {@link JpaRepository}.
 * Esta interfaz no define métodos personalizados, pero está configurada para ser expuesta como un recurso REST
 * mediante {@link RepositoryRestResource}.</p>
 *
 * <p>Configuraciones específicas:
 * <ul>
 *   <li><b>@Repository:</b> Marca esta interfaz como un componente de acceso a datos en el contexto de Spring.</li>
 *   <li><b>@RepositoryRestResource:</b> Personaliza la exposición del repositorio como un recurso REST:
 *     <ul>
 *       <li><b>path:</b> Define la ruta base para los recursos relacionados con <code>Respuesta</code> como <code>/respuestas</code>.</li>
 *       <li><b>collectionResourceRel:</b> Establece el nombre del enlace de colección en las respuestas JSON como <code>respuestas</code>.</li>
 *     </ul>
 *   </li>
 * </ul>
 * </p>
 *
 * <p>Ejemplo de uso REST:
 * <ul>
 *   <li><code>GET /respuestas</code>: Obtiene una lista de respuestas.</li>
 *   <li><code>POST /respuestas</code>: Crea una nueva respuesta.</li>
 *   <li><code>PUT /respuestas/{id}</code>: Actualiza una respuesta existente.</li>
 *   <li><code>DELETE /respuestas/{id}</code>: Elimina una respuesta por ID.</li>
 * </ul>
 * </p>
 *
 * <h3>Métodos Predeterminados</h3>
 * <p>Esta interfaz no define métodos personalizados, pero hereda los métodos básicos de {@link JpaRepository}, como:</p>
 * <ul>
 *   <li><code>save</code>: Guarda o actualiza una entidad.</li>
 *   <li><code>findById</code>: Busca una entidad por su ID.</li>
 *   <li><code>findAll</code>: Devuelve todas las entidades.</li>
 *   <li><code>deleteById</code>: Elimina una entidad por su ID.</li>
 * </ul>
 *
 * @see JpaRepository
 * @see RepositoryRestResource
 */
@Repository
@RepositoryRestResource(path = "respuestas", collectionResourceRel = "respuestas")

public interface RespuestaRepository extends JpaRepository <Respuesta, Long>{
}