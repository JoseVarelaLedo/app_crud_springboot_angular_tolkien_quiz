package app.rest.controller;

import app.dto.FichaUsuarioDTO;
import app.exceptions.ResourceNotFoundException;
import app.model.FichaUsuario;
import app.service.FichaUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/fichasUsuario")
@CrossOrigin(origins = "http://localhost:4200/")

public class FichaUsuarioRestController {
    @Autowired
    private FichaUsuarioService fichaUsuarioService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FichaUsuarioRestController.class);


    @GetMapping ("/lista")
    public ResponseEntity<Page<FichaUsuarioDTO>> listarFichasDeUSuario(
            @RequestParam int pag,
            @RequestParam int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion,
            @RequestParam (required = false) String filtro){
        // Llama al servicio y devuelve la respuesta paginada
        return ResponseEntity.ok(fichaUsuarioService.listarFichasDeUsuarios(pag, tam, campoOrdenacion, direccionOrdenacion, filtro));
    }

    @GetMapping ("/buscarUsuarioPorId/id/{id}")
    public ResponseEntity<FichaUsuario> getUSuarioPorId(@PathVariable Long id){
        try{
            FichaUsuario fichaUsuario = fichaUsuarioService.obtenerFichasDeUsuariosPorId(id);
            return ResponseEntity.ok (fichaUsuario);
        }catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException("Ficha de usuario " + id + " no encontrada");
        }
    }

    @PostMapping("/crearFichaDeUsuario")
    public ResponseEntity<FichaUsuario> crearFichaDeUsuario(@RequestBody FichaUsuarioDTO fichaUsuarioDTO) {
        LOGGER.info("Solicitud recibida para crear ficha de usuario: {}", fichaUsuarioDTO);
        try{
            FichaUsuario nuevoFichaUsuario = fichaUsuarioService.crearFichasDeUsuarios(fichaUsuarioDTO);
            LOGGER.info("Ficha de usuario creada con éxito: {}", nuevoFichaUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoFichaUsuario);
        }catch(Exception e){
            LOGGER.error("Error al crear ficha de usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/editar/id/{id}")
    public ResponseEntity<FichaUsuario> actualizarFichaUsuario(@PathVariable Long id, @RequestBody FichaUsuarioDTO fichaUsuarioDTO) {
        FichaUsuario fichaUsuarioActualizado = fichaUsuarioService.actualizarFichasDeUsuarios(id, fichaUsuarioDTO);
        return ResponseEntity.ok(fichaUsuarioActualizado);
    }

    @DeleteMapping("/eliminar/id/{id}")
    public ResponseEntity<Map<String, Boolean>> borrarUsuario(@PathVariable Long id) {
        try {
            FichaUsuario fichaUsuario = fichaUsuarioService.obtenerFichasDeUsuariosPorId(id);
            if (fichaUsuario == null) {
                throw new ResourceNotFoundException("Ficha de usuario " + id + " no encontrada");
            }
            fichaUsuarioService.borrarEmpleado(id);

            // Respuesta de éxito
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Ficha de usuario " + id + " no encontrada");
        }
    }
}
