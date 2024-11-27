package app.rest.controller;

import app.dto.FichaUsuarioDTO;
import app.exceptions.ResourceNotFoundException;
import app.model.Categoria;
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

/**
 * Controlador REST para la clase {@link FichaUsuario}.
 * Se anota con @RestController para que SpringBoot lo reconozca como tal.
 * @RequestMapping: indicamos el endpoint al que se apuntará para los métodos GET/POST/PUT/DELETE INVOCADOS
 * @CrossOrigin: indicamos la URL del frontend con el que se comunicará el frontend.
 * Se comunica con una entidad del servicio en el que se define el comportamiento de cada aacción invocada,
 * autoinyectada mediante la anotación @Autowired
 */
@RestController
@RequestMapping("/fichasUsuario")
@CrossOrigin(origins = "http://localhost:4200/")

public class FichaUsuarioRestController {
    @Autowired
    private FichaUsuarioService fichaUsuarioService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FichaUsuarioRestController.class);

    /**
     * Obtiene una lista paginada de fichas de usuario, con opciones de filtro y ordenación.
     *
     * @param pag número de página.
     * @param tam tamaño de página.
     * @param campoOrdenacion campo para ordenar los resultados (por defecto: id).
     * @param direccionOrdenacion dirección de ordenación: "asc" o "desc" (por defecto: asc).
     * @param filtro filtro opcional para buscar fichas de usuario por atributos.
     * @return una página de fichas de usuario.
     */
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

    /**
     * Obtiene una ficha de usuario por su ID.
     *
     * @param id identificador único de la ficha de usuario.
     * @return la ficha de usuario encontrada o lanza una excepción si no se encuentra.
     */
    @GetMapping ("/buscarUsuarioPorId/id/{id}")
    public ResponseEntity<FichaUsuario> getUSuarioPorId(@PathVariable Long id){
        try{
            FichaUsuario fichaUsuario = fichaUsuarioService.obtenerFichasDeUsuariosPorId(id);
            return ResponseEntity.ok (fichaUsuario);
        }catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException("Ficha de usuario " + id + " no encontrada");
        }
    }

    /**
     * Crea una nueva ficha de usuario.
     *
     * @param fichaUsuarioDTO datos de la ficha de usuario a crear.
     * @return la ficha de usuario creada con un estado HTTP 201.
     */
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

    /**
     * Actualiza una ficha de usuario existente.
     *
     * @param id identificador de la ficha de usuario a actualizar.
     * @param fichaUsuarioDTO datos actualizados para la ficha de usuario.
     * @return la ficha de usuario actualizada.
     */
    @PutMapping("/editar/id/{id}")
    public ResponseEntity<FichaUsuario> actualizarFichaUsuario(@PathVariable Long id, @RequestBody FichaUsuarioDTO fichaUsuarioDTO) {
        FichaUsuario fichaUsuarioActualizado = fichaUsuarioService.actualizarFichasDeUsuarios(id, fichaUsuarioDTO);
        return ResponseEntity.ok(fichaUsuarioActualizado);
    }

    /**
     * Elimina una ficha de usuario por su ID.
     *
     * @param id identificador de la ficha de usuario a eliminar.
     * @return un mapa indicando que la eliminación fue exitosa.
     */
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
