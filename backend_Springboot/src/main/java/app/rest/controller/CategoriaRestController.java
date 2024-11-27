package app.rest.controller;

import app.model.Categoria;
import app.exceptions.ResourceNotFoundException;
import app.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la clase {@link Categoria}.
 * Se anota con @RestController para que SpringBoot lo reconozca como tal.
 * @RequestMapping: indicamos el endpoint al que se apuntará para los métodos GET/POST/PUT/DELETE INVOCADOS
 * @CrossOrigin: indicamos la URL del frontend con el que se comunicará el frontend.
 * Se comunica con una entidad del servicio en el que se define el comportamiento de cada aacción invocada,
 * autoinyectada mediante la anotación @Autowired
 */

@RestController
@RequestMapping("/categorias")
@CrossOrigin (origins = "http://localhost:4200/")
public class CategoriaRestController {

    @Autowired
    private CategoriaService categoriaService;

    /**
     * Obtiene una lista paginada de categorías, con opciones de ordenación.
     *
     * @param pag número de página (por defecto: 0).
     * @param tam tamaño de página (por defecto: 7).
     * @param campoOrdenacion campo para ordenar los resultados (por defecto: id).
     * @param direccionOrdenacion dirección de ordenación: "asc" o "desc" (por defecto: asc).
     * @return una página de categorías.
     */
    @GetMapping ("/lista")
    public ResponseEntity <Page<Categoria>> getAll(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion){
        return ResponseEntity.ok(categoriaService.listarCategorias(pag, tam, campoOrdenacion, direccionOrdenacion));
    }

    /**
     * Obtiene una lista completa de categorías sin paginación.
     *
     * @return una lista de todas las categorías.
     */
    @GetMapping("/noPag")
    public ResponseEntity<List<Categoria>> getAllNoPag(){
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    /**
     * Obtiene una categoría por su ID.
     *
     * @param id identificador único de la categoría.
     * @return la categoría encontrada o lanza una excepción si no se encuentra.
     */
    @GetMapping ("/buscarCategoriaPorId/id/{id}")
    public ResponseEntity<Categoria> getCategoriaPorId(@PathVariable Long id){
        try{
            Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
            return ResponseEntity.ok (categoria);
        }catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException("Categoria " + id + " no encontrada");
        }
    }

    /**
     * Crea una nueva categoría.
     *
     * @param categoria datos de la categoría a crear.
     * @return la categoría creada con un estado HTTP 201.
     */
    @PostMapping ("/crearCategoria")
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        try {
            categoriaService.crearCategoria(categoria);
            return new ResponseEntity<>(categoria, HttpStatus.CREATED);                 // retorna 201 CREATED
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);        // si ocurre un error, retorna 500
        }
    }

    /**
     * Actualiza una categoría existente.
     *
     * @param id identificador de la categoría a actualizar.
     * @param datosActualizados datos actualizados para la categoría.
     * @return la categoría actualizada.
     */
    @PutMapping("/editar/id/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria datosActualizados) {
        try {
            Categoria categoriaExistente = categoriaService.obtenerCategoriaPorId(id);

            Categoria categoriaActualizada = Categoria.builder()                    // se crea un nuevo objeto combinando valores viejos y nuevos usando Builder de Lombok
                    .id(categoriaExistente.getId())                                 // se mantiene el mismo ID
                    .categoriaDesc(datosActualizados.getCategoriaDesc() != null ? datosActualizados.getCategoriaDesc() : categoriaExistente.getCategoriaDesc())
                    .build();

            categoriaService.actualizarCategoria(categoriaActualizada);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Categoria " + id + " no encontrada");
        }
    }

    /**
     * Elimina una categoría por su ID.
     *
     * @param id identificador de la categoría a eliminar.
     * @return un mapa indicando que la eliminación fue exitosa.
     */
    @DeleteMapping("/eliminar/id/{id}")
    public ResponseEntity<Map<String, Boolean>> borrarCategoria(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
            if (categoria == null) {
                throw new ResourceNotFoundException("Categoria " + id + " no encontrada");
            }
            categoriaService.borrarCategoria(id);

            Map<String, Boolean> response = new HashMap<>();   // respuesta de éxito
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Categoria " + id + " no encontrada");
        }
    }
}
