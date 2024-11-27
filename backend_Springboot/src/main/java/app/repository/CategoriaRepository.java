package app.repository;

import app.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de datos para la entidad {@link Categoria}.
 *
 * <p>Proporciona acceso a las operaciones CRUD básicas y funcionalidades adicionales
 * heredadas de {@link JpaRepository}. Esta interfaz es administrada por Spring Data JPA
 * y expuesta como un recurso REST gracias a la anotación {@link RepositoryRestResource}.</p>
 *
 * <p>Configuraciones específicas:
 * <ul>
 *   <li><b>@Repository:</b> Marca esta interfaz como un componente de acceso a datos en el contexto de Spring.</li>
 *   <li><b>@RepositoryRestResource:</b> Personaliza la exposición del repositorio como un recurso REST:
 *     <ul>
 *       <li><b>path:</b> Define la ruta base para los recursos relacionados con <code>Categoria</code> como <code>/categorias</code>.</li>
 *       <li><b>collectionResourceRel:</b> Establece el nombre del enlace de colección en las respuestas JSON como <code>categorias</code>.</li>
 *     </ul>
 *   </li>
 * </ul>
 * </p>
 *
 * <p>Ejemplo de uso REST:
 * <ul>
 *   <li><code>GET /categorias</code>: Obtiene una lista de categorías.</li>
 *   <li><code>POST /categorias</code>: Crea una nueva categoría.</li>
 *   <li><code>PUT /categorias/{id}</code>: Actualiza una categoría existente.</li>
 *   <li><code>DELETE /categorias/{id}</code>: Elimina una categoría por ID.</li>
 * </ul>
 * </p>
 *
 * @see JpaRepository
 * @see RepositoryRestResource
 */
@Repository
@RepositoryRestResource(path = "categorias", collectionResourceRel = "categorias")

public interface CategoriaRepository extends JpaRepository <Categoria, Long>{
}