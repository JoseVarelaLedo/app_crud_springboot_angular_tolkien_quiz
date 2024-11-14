package app.rest.controller;

import app.dto.UsuarioDTO;
import app.exceptions.ResourceNotFoundException;
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

@RestController
@RequestMapping("/usuarios")
@CrossOrigin (origins = "http://localhost:4200/")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    private ResponseEntity<Page<UsuarioDTO>> getTodosUsuarios(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion){
        return ResponseEntity.ok(usuarioService.listarUsuarios(pag, tam, campoOrdenacion, direccionOrdenacion));
    }

    @GetMapping ("/buscarUsuarioPorId/id/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        UsuarioDTO usuarioDTO = usuarioService.convertirADTO(usuario);
        return ResponseEntity.ok(usuarioDTO);
    }

    @GetMapping("/porRol")
    public Page<UsuarioDTO> getUsuariosPorRol(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion,
            @RequestParam("rolId") Integer rolId) {
        return usuarioService.buscarUsuarioPorRolId(pag, tam, campoOrdenacion, direccionOrdenacion, rolId);
    }

    @GetMapping("/buscarNickname")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorNickname(@RequestParam("nickname") String nickname) {
        Optional<UsuarioDTO> usuario = usuarioService.buscarUsuarioPorNickName(nickname);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

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


    @GetMapping("/verificarNickname")
    public ResponseEntity<Boolean> verificarNickname(@RequestParam String nickname) {
        boolean exists = usuarioService.existsByNickname(nickname);
        return ResponseEntity.ok(exists);
    }


    @PostMapping("/crearUsuario")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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