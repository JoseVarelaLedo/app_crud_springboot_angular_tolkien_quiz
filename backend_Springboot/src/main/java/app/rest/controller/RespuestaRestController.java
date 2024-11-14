package app.rest.controller;

import app.dto.RespuestaDTO;
import app.exceptions.ResourceNotFoundException;
import app.model.Respuesta;
import app.service.RespuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/respuestas")
@CrossOrigin (origins = "http://localhost:4200/")
public class RespuestaRestController {

    @Autowired
    private RespuestaService respuestaService;

    @GetMapping
    private ResponseEntity <Page<RespuestaDTO>> getAll(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion){
        return ResponseEntity.ok(respuestaService.listarRespuestas(pag, tam, campoOrdenacion, direccionOrdenacion));
    }

    @GetMapping ("/buscarRespuestaPorId/id/{id}")
    public ResponseEntity<Respuesta> getRespuestaPorId(@PathVariable Long id){
        try{
            Respuesta respuesta = respuestaService.obtenerRespuestaPorId(id);
            return ResponseEntity.ok (respuesta);
        }catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException("Respuesta " + id + " no encontrada");
        }
    }

    @PostMapping("/crearRespuesta")
    public ResponseEntity<Respuesta> crearRespuesta(@RequestBody RespuestaDTO respuestaDTO) {
        Respuesta nuevaRespuesta = respuestaService.crearRespuesta(respuestaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRespuesta);
    }

    @PutMapping("/editar/id/{id}")
    public ResponseEntity<Respuesta> updateRespuesta(@PathVariable Long id, @RequestBody RespuestaDTO datosActualizados) {
        Respuesta respuestaActualizada = respuestaService.actualizarRespuesta(id, datosActualizados);
        return ResponseEntity.ok(respuestaActualizada);
    }

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
