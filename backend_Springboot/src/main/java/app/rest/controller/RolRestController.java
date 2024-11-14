package app.rest.controller;

import app.model.Rol;
import app.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "http://localhost:4200/")
public class RolRestController {
    @Autowired
    private RolService rolService;

    @GetMapping
    private ResponseEntity<List<Rol>> listarRoles(){
        return ResponseEntity.ok(rolService.listarTodosLosRoles());
    }

}
