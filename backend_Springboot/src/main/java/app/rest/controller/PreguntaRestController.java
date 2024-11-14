package app.rest.controller;

import app.exceptions.ResourceNotFoundException;
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

@RestController
@RequestMapping("/preguntas")
@CrossOrigin (origins = "http://localhost:4200/")
public class PreguntaRestController {

    @Autowired
    private PreguntaService preguntaService;

    @GetMapping
    private ResponseEntity<Page<Pregunta>> getAll(
        @RequestParam(defaultValue = "0") int pag,
        @RequestParam(defaultValue = "7") int tam,
        @RequestParam(defaultValue = "id") String campoOrdenacion,
        @RequestParam(defaultValue = "asc") String direccionOrdenacion,
        @RequestParam(required = false) Long categoriaId) {                                                                     // añadir el nuevo parámetro
        return ResponseEntity.ok(preguntaService.listarPreguntas(pag, tam, campoOrdenacion, direccionOrdenacion, categoriaId));
    }

    @GetMapping ("/contenido/{textoPregunta}")
    public ResponseEntity<Page<Pregunta>> getPreguntasFiltradasPorContenido(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion,
            @PathVariable("textoPregunta") String textoPregunta) {
        return ResponseEntity.ok(preguntaService.encontrarPreguntasPorContenido(pag,tam,campoOrdenacion,direccionOrdenacion,textoPregunta));
    }

    @GetMapping ("/categoria/{categoriaId}")
    public ResponseEntity<Page<Pregunta>> getPreguntasFiltradasPorCategoria(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion,
            @PathVariable("categoriaId") Integer categoriaId) {
        return ResponseEntity.ok(preguntaService.encontrarPreguntasPorCategoria(pag,tam,campoOrdenacion,direccionOrdenacion,categoriaId));
    }


    @GetMapping ("/buscarPreguntaPorId/id/{id}")
    public ResponseEntity<Pregunta> getPreguntaPorId(@PathVariable Long id){
        try{
            Pregunta pregunta = preguntaService.obtenerPreguntaPorId(id);
            return ResponseEntity.ok (pregunta);
        }catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException("Pregunta " + id + " no encontrada");
        }
    }

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

    @PutMapping("/editar/id/{id}")
    public ResponseEntity<Pregunta> updatePregunta(@PathVariable Long id, @RequestBody Pregunta datosActualizados) {
        try {
            preguntaService.actualizarPregunta(datosActualizados);
            return ResponseEntity.ok(datosActualizados);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Pregunta " + id + " no encontrada");
        }
    }

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
