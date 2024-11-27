package app.rest.controller;

import app.model.Respuesta;
import app.model.Rol;
import app.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para la clase {@link Rol}.
 * Se anota con @RestController para que SpringBoot lo reconozca como tal.
 * @RequestMapping: indicamos el endpoint al que se apuntará para los métodos GET/POST/PUT/DELETE INVOCADOS
 * @CrossOrigin: indicamos la URL del frontend con el que se comunicará el frontend.
 * Se comunica con una entidad del servicio en el que se define el comportamiento de cada aacción invocada,
 * autoinyectada mediante la anotación @Autowired
 */
@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "http://localhost:4200/")
public class RolRestController {
    @Autowired
    private RolService rolService;

    /**
     * Obtiene una lista de todos los roles disponibles.
     *
     * @return una lista de roles con un estado HTTP 200.
     */
    @GetMapping ("/lista")
    private ResponseEntity<List<Rol>> listarRoles(){
        return ResponseEntity.ok(rolService.listarTodosLosRoles());
    }
}
