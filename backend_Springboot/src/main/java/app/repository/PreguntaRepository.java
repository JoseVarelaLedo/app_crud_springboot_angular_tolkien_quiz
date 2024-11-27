package app.repository;

import app.model.Pregunta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
/**
 * Repositorio de datos para la entidad {@link Pregunta}.
 *
 * <p>Proporciona acceso a las operaciones CRUD básicas y funcionalidades adicionales heredadas de {@link JpaRepository}.
 * Además, incluye métodos personalizados para buscar preguntas por categoría o por contenido textual, con soporte de paginación.</p>
 *
 * <p>Configuraciones específicas:
 * <ul>
 *   <li><b>@Repository:</b> Marca esta interfaz como un componente de acceso a datos en el contexto de Spring.</li>
 *   <li><b>@RepositoryRestResource:</b> Personaliza la exposición del repositorio como un recurso REST:
 *     <ul>
 *       <li><b>path:</b> Define la ruta base para los recursos relacionados con <code>Pregunta</code> como <code>/preguntas</code>.</li>
 *       <li><b>collectionResourceRel:</b> Establece el nombre del enlace de colección en las respuestas JSON como <code>preguntas</code>.</li>
 *     </ul>
 *   </li>
 * </ul>
 * </p>
 *
 * <p>Ejemplo de uso REST:
 * <ul>
 *   <li><code>GET /preguntas</code>: Obtiene una lista de preguntas.</li>
 *   <li><code>POST /preguntas</code>: Crea una nueva pregunta.</li>
 *   <li><code>PUT /preguntas/{id}</code>: Actualiza una pregunta existente.</li>
 *   <li><code>DELETE /preguntas/{id}</code>: Elimina una pregunta por ID.</li>
 * </ul>
 * </p>
 *
 * <h3>Métodos Personalizados</h3>
 * <ul>
 *   <li><b>findByCategoriaId:</b> Busca preguntas asociadas a una categoría específica.</li>
 *   <li><b>findByTextoPreguntaContainingIgnoreCase:</b> Realiza una búsqueda en el contenido de las preguntas sin distinguir mayúsculas y minúsculas.</li>
 *   <li><b>findQuestionByCategoryId:</b> Busca preguntas por categoría, permitiendo manejar categorías opcionales (<code>null</code>).</li>
 * </ul>
 *
 * @see JpaRepository
 * @see RepositoryRestResource
 */

@Repository
@RepositoryRestResource(path = "preguntas`", collectionResourceRel = "preguntas")

public interface PreguntaRepository extends JpaRepository <Pregunta, Long>{
    Page<Pregunta> findByCategoriaId(Long categoriaId, Pageable pageable);
    Page<Pregunta> findByTextoPreguntaContainingIgnoreCase(String textoPregunta, Pageable pageable);
    @Query("SELECT p FROM Pregunta p WHERE (:categoriaId IS NULL OR p.categoria.id = :categoriaId)")
    Page <Pregunta> findQuestionByCategoryId (@Param("categoriaId")Integer categoriaId, Pageable pageable);
}

