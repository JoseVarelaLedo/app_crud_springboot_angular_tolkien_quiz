package app.rest.controller;

import app.dto.UsuarioDTO;
import app.exceptions.ResourceNotFoundException;
import app.model.Respuesta;
import app.model.Usuario;
import app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para la clase {@link Usuario}.
 * Se anota con @RestController para que SpringBoot lo reconozca como tal.
 * @RequestMapping: indicamos el endpoint al que se apuntará para los métodos GET/POST/PUT/DELETE INVOCADOS
 * @CrossOrigin: indicamos la URL del frontend con el que se comunicará el frontend.
 * Se comunica con una entidad del servicio en el que se define el comportamiento de cada aacción invocada,
 * autoinyectada mediante la anotación @Autowired
 */
@RestController
@RequestMapping("/usuarios")
@CrossOrigin (origins = "http://localhost:4200/")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene una lista paginada de todos los usuarios.
     *
     * @param pag número de página (por defecto: 0).
     * @param tam tamaño de página (por defecto: 7).
     * @param campoOrdenacion campo por el cual ordenar (por defecto: "id").
     * @param direccionOrdenacion dirección de la ordenación: ascendente ("asc") o descendente ("desc").
     * @return lista paginada de usuarios con un estado HTTP 200.
     */
    @GetMapping ("/lista")
    public ResponseEntity<Page<UsuarioDTO>> getTodosUsuarios(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion){
        return ResponseEntity.ok(usuarioService.listarUsuarios(pag, tam, campoOrdenacion, direccionOrdenacion));
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id identificador único del usuario.
     * @return información del usuario como un DTO si se encuentra, o un estado HTTP 404 si no existe.
     */
    @GetMapping ("/buscarUsuarioPorId/id/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        UsuarioDTO usuarioDTO = usuarioService.convertirADTO(usuario);
        return ResponseEntity.ok(usuarioDTO);
    }

    /**
     * Obtiene usuarios por su rol, de forma paginada.
     *
     * @param pag número de página (por defecto: 0).
     * @param tam tamaño de página (por defecto: 7).
     * @param campoOrdenacion campo por el cual ordenar (por defecto: "id").
     * @param direccionOrdenacion dirección de la ordenación: ascendente ("asc") o descendente ("desc").
     * @param rolId identificador del rol para filtrar los usuarios.
     * @return lista paginada de usuarios con el rol especificado.
     */
    @GetMapping("/porRol")
    public Page<UsuarioDTO> getUsuariosPorRol(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion,
            @RequestParam("rolId") Integer rolId) {
        return usuarioService.buscarUsuarioPorRolId(pag, tam, campoOrdenacion, direccionOrdenacion, rolId);
    }

    /**
     * Busca un usuario por su nickname.
     *
     * @param nickname nombre único del usuario.
     * @return información del usuario como un DTO si se encuentra, o un estado HTTP 404 si no existe.
     */
    @GetMapping("/buscarNickname")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorNickname(@RequestParam("nickname") String nickname) {
        Optional<UsuarioDTO> usuario = usuarioService.buscarUsuarioPorNickName(nickname);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene usuarios registrados después de una fecha específica.
     *
     * @param fecha fecha de registro para filtrar usuarios.
     * @param pag número de página (por defecto: 0).
     * @param tam tamaño de página (por defecto: 7).
     * @param campoOrdenacion campo por el cual ordenar (por defecto: "id").
     * @param direccionOrdenacion dirección de la ordenación: ascendente ("asc") o descendente ("desc").
     * @return lista paginada de usuarios registrados después de la fecha especificada.
     */
    @GetMapping("/buscarPorFecha")
    public ResponseEntity<Page<UsuarioDTO>> buscarUsuariosPorFecha(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha,
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion) {
        System.out.println (fecha.toString());
        Page<UsuarioDTO> usuarios = usuarioService.buscarUsuariosPorFecha(fecha, pag, tam, campoOrdenacion, direccionOrdenacion);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Verifica si un nickname existe en el sistema.
     *
     * @param nickname nombre único del usuario.
     * @return true si el nickname existe, false en caso contrario.
     */
    @GetMapping("/verificarNickname")
    public ResponseEntity<Boolean> verificarNickname(@RequestParam String nickname) {
        boolean exists = usuarioService.existsByNickname(nickname);
        return ResponseEntity.ok(exists);
    }

    /**
     * Crea un nuevo usuario basado en los datos proporcionados.
     *
     * @param usuarioDTO información del nuevo usuario.
     * @return el usuario creado con un estado HTTP 201, o un estado HTTP 500 en caso de error.
     */
    @PostMapping("/crearUsuario")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param id identificador único del usuario a actualizar.
     * @param datosActualizados datos actualizados del usuario.
     * @return el usuario actualizado con un estado HTTP 200, o un estado HTTP 404 si no se encuentra.
     */
    @PutMapping("/editar/id/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody UsuarioDTO datosActualizados) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, datosActualizados);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioActualizado);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // usuario no encontrado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // error interno
        }
    }

    /**
     * Elimina un usuario basado en su ID.
     *
     * @param id identificador único del usuario.
     * @return un mapa indicando si el usuario fue eliminado con un estado HTTP 200, o un estado HTTP 404 si no se encuentra.
     */
    @DeleteMapping("/eliminar/id/{id}")
    public ResponseEntity<Map<String, Boolean>> borrarUsuario(@PathVariable Long id) {
        try {
            Usuario usurio = usuarioService.obtenerUsuarioPorId(id);
            if (usurio == null) {
                throw new ResourceNotFoundException("Usuario " + id + " no encontrado");
            }
            usuarioService.borrarUsuario(id);

            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Usuario " + id + " no encontrado");
        }
    }
}