package app.rest.controller;

import app.dto.RespuestaDTO;
import app.exceptions.ResourceNotFoundException;
import app.model.Pregunta;
import app.model.Respuesta;
import app.service.RespuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para la clase {@link Respuesta}.
 * Se anota con @RestController para que SpringBoot lo reconozca como tal.
 * @RequestMapping: indicamos el endpoint al que se apuntará para los métodos GET/POST/PUT/DELETE INVOCADOS
 * @CrossOrigin: indicamos la URL del frontend con el que se comunicará el frontend.
 * Se comunica con una entidad del servicio en el que se define el comportamiento de cada aacción invocada,
 * autoinyectada mediante la anotación @Autowired
 */
@RestController
@RequestMapping("/respuestas")
@CrossOrigin (origins = "http://localhost:4200/")
public class RespuestaRestController {

    @Autowired
    private RespuestaService respuestaService;

    /**
     * Obtiene una lista paginada de respuestas con opciones de ordenación.
     *
     * @param pag número de página (por defecto: 0).
     * @param tam tamaño de página (por defecto: 7).
     * @param campoOrdenacion campo para ordenar los resultados (por defecto: id).
     * @param direccionOrdenacion dirección de ordenación: "asc" o "desc" (por defecto: asc).
     * @return una página de respuestas.
     */
    @GetMapping
    public ResponseEntity <Page<RespuestaDTO>> getAll(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion){
        return ResponseEntity.ok(respuestaService.listarRespuestas(pag, tam, campoOrdenacion, direccionOrdenacion));
    }

    /**
     * Obtiene una respuesta por su ID.
     *
     * @param id identificador único de la respuesta.
     * @return la respuesta encontrada o lanza una excepción si no se encuentra.
     */
    @GetMapping ("/buscarRespuestaPorId/id/{id}")
    public ResponseEntity<Respuesta> getRespuestaPorId(@PathVariable Long id){
        try{
            Respuesta respuesta = respuestaService.obtenerRespuestaPorId(id);
            return ResponseEntity.ok (respuesta);
        }catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException("Respuesta " + id + " no encontrada");
        }
    }

    /**
     * Crea una nueva respuesta a partir de los datos proporcionados.
     *
     * @param respuestaDTO datos de la nueva respuesta.
     * @return la respuesta creada con un estado HTTP 201.
     */
    @PostMapping("/crearRespuesta")
    public ResponseEntity<Respuesta> crearRespuesta(@RequestBody RespuestaDTO respuestaDTO) {
        Respuesta nuevaRespuesta = respuestaService.crearRespuesta(respuestaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRespuesta);
    }

    /**
     * Actualiza una respuesta existente.
     *
     * @param id identificador de la respuesta a actualizar.
     * @param datosActualizados datos actualizados de la respuesta.
     * @return la respuesta actualizada.
     */
    @PutMapping("/editar/id/{id}")
    public ResponseEntity<Respuesta> updateRespuesta(@PathVariable Long id, @RequestBody RespuestaDTO datosActualizados) {
        Respuesta respuestaActualizada = respuestaService.actualizarRespuesta(id, datosActualizados);
        return ResponseEntity.ok(respuestaActualizada);
    }

    /**
     * Elimina una respuesta por su ID.
     *
     * @param id identificador de la respuesta a eliminar.
     * @return un mapa indicando que la eliminación fue exitosa.
     */
    @DeleteMapping("/eliminar/id/{id}")
    public ResponseEntity<Map<String, Boolean>> borrarRespuesta(@PathVariable Long id) {
        try {
            Respuesta respuesta = respuestaService.obtenerRespuestaPorId(id);
            if (respuesta == null) {
                throw new ResourceNotFoundException("Respuesta " + id + " no encontrada");
            }
            respuestaService.borrarRespuesta(id);

            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Respuesta " + id + " no encontrada");
        }
    }
}
