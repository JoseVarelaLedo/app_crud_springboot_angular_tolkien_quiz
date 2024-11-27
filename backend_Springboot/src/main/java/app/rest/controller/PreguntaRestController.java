package app.rest.controller;

import app.exceptions.ResourceNotFoundException;
import app.model.FichaUsuario;
import app.model.Pregunta;
import app.model.Respuesta;
import app.service.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Controlador REST para la clase {@link Pregunta}.
 * Se anota con @RestController para que SpringBoot lo reconozca como tal.
 * @RequestMapping: indicamos el endpoint al que se apuntará para los métodos GET/POST/PUT/DELETE INVOCADOS
 * @CrossOrigin: indicamos la URL del frontend con el que se comunicará el frontend.
 * Se comunica con una entidad del servicio en el que se define el comportamiento de cada aacción invocada,
 * autoinyectada mediante la anotación @Autowired
 */
@RestController
@RequestMapping("/preguntas")
@CrossOrigin (origins = "http://localhost:4200/")
public class PreguntaRestController {

    @Autowired
    private PreguntaService preguntaService;

    /**
     * Obtiene una lista paginada de preguntas, con opciones de filtro y ordenación.
     *
     * @param pag número de página.
     * @param tam tamaño de página.
     * @param campoOrdenacion campo para ordenar los resultados (por defecto: id).
     * @param direccionOrdenacion dirección de ordenación: "asc" o "desc" (por defecto: asc).
     * @param categoriaId (opcional) ID de la categoría para filtrar preguntas.
     * @return una página de preguntas.
     */
    @GetMapping ("/lista")
    public ResponseEntity<Page<Pregunta>> getAll(
        @RequestParam(defaultValue = "0") int pag,
        @RequestParam(defaultValue = "7") int tam,
        @RequestParam(defaultValue = "id") String campoOrdenacion,
        @RequestParam(defaultValue = "asc") String direccionOrdenacion,
        @RequestParam(required = false) Long categoriaId) {                                                                     // añadir el nuevo parámetro
        return ResponseEntity.ok(preguntaService.listarPreguntas(pag, tam, campoOrdenacion, direccionOrdenacion, categoriaId));
    }

    /**
     * Obtiene una lista paginada de preguntas cuyo contenido contiene un texto específico.
     *
     * @param pag número de página.
     * @param tam tamaño de página.
     * @param campoOrdenacion campo para ordenar los resultados.
     * @param direccionOrdenacion dirección de ordenación: "asc" o "desc".
     * @param textoPregunta texto a buscar en el contenido de las preguntas.
     * @return una página de preguntas que coinciden con el texto especificado.
     */
    @GetMapping ("/contenido/{textoPregunta}")
    public ResponseEntity<Page<Pregunta>> getPreguntasFiltradasPorContenido(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion,
            @PathVariable("textoPregunta") String textoPregunta) {
        return ResponseEntity.ok(preguntaService.encontrarPreguntasPorContenido(pag,tam,campoOrdenacion,direccionOrdenacion,textoPregunta));
    }

    /**
     * Obtiene una lista paginada de preguntas pertenecientes a una categoría específica.
     *
     * @param pag número de página.
     * @param tam tamaño de página.
     * @param campoOrdenacion campo para ordenar los resultados.
     * @param direccionOrdenacion dirección de ordenación: "asc" o "desc".
     * @param categoriaId ID de la categoría.
     * @return una página de preguntas que pertenecen a la categoría especificada.
     */
    @GetMapping ("/categoria/{categoriaId}")
    public ResponseEntity<Page<Pregunta>> getPreguntasFiltradasPorCategoria(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion,
            @PathVariable("categoriaId") Integer categoriaId) {
        return ResponseEntity.ok(preguntaService.encontrarPreguntasPorCategoria(pag,tam,campoOrdenacion,direccionOrdenacion,categoriaId));
    }

    /**
     * Obtiene una pregunta por su ID.
     *
     * @param id identificador único de la pregunta.
     * @return la pregunta encontrada o lanza una excepción si no se encuentra.
     */
    @GetMapping ("/buscarPreguntaPorId/id/{id}")
    public ResponseEntity<Pregunta> getPreguntaPorId(@PathVariable Long id){
        try{
            Pregunta pregunta = preguntaService.obtenerPreguntaPorId(id);
            return ResponseEntity.ok (pregunta);
        }catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException("Pregunta " + id + " no encontrada");
        }
    }

    /**
     * Crea una nueva pregunta.
     *
     * @param pregunta datos de la pregunta a crear.
     * @return la pregunta creada con un estado HTTP 201.
     */
    @PostMapping("/crearPregunta")
    public ResponseEntity<Pregunta> crearPregunta(@RequestBody Pregunta pregunta) {
        List<Respuesta> respuestas = pregunta.getRespuestas();
        for (Respuesta respuesta : respuestas) {
            respuesta.setPregunta(pregunta);
        }
        try {
            preguntaService.crearPregunta(pregunta);
            return new ResponseEntity<>(pregunta, HttpStatus.CREATED);                  // retorna 201 CREATED
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);        // Si ocurre un error, retorna 500
        }
    }

    /**
     * Actualiza una pregunta existente.
     *
     * @param id identificador de la pregunta a actualizar.
     * @param datosActualizados datos actualizados de la pregunta.
     * @return la pregunta actualizada.
     */
    @PutMapping("/editar/id/{id}")
    public ResponseEntity<Pregunta> updatePregunta(@PathVariable Long id, @RequestBody Pregunta datosActualizados) {
        try {
            preguntaService.actualizarPregunta(datosActualizados);
            return ResponseEntity.ok(datosActualizados);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Pregunta " + id + " no encontrada");
        }
    }

    /**
     * Elimina una pregunta por su ID.
     *
     * @param id identificador de la pregunta a eliminar.
     * @return un mapa indicando que la eliminación fue exitosa.
     */
    @DeleteMapping("/eliminar/id/{id}")
    public ResponseEntity<Map<String, Boolean>> borrarPregunta(@PathVariable Long id) {
        try {
            Pregunta pregunta = preguntaService.obtenerPreguntaPorId(id);
            if (pregunta == null) {
                throw new ResourceNotFoundException("Pregunta " + id + " no encontrada");
            }
            preguntaService.borrarPregunta(id);

            Map<String, Boolean> response = new HashMap<>();             // respuesta de éxito
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Pregunta " + id + " no encontrada");
        }
    }
}
