package app.data;

import app.model.Rol;
import app.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Esta clase inicializa la tabla de roles con los datos necesarios;
 * se inicia en segundo lugar dentro de las clases inicializadoras ya que hemos
 * anotado con @Order(2), para que se inicie antes que las fichas de usuario,
 * al tener esta última tabla dependencias con los roles.  Se anota con @Order para que el framework Spring la trate de acuerdo a su naturaleza.
 */
@Component
@Order(2)
public class DataInitializerRol {

    @Autowired
    private RolRepository rolRepository;

    @Bean
    public ApplicationRunner initializerRol(RolRepository rolRepository) {
        return args -> {
            if (rolRepository.count() == 0) {
                insertarRoles(this.rolRepository);
            }
        };
    }

    public void insertarRoles(RolRepository rolRepository) {
        List<Rol> roles = Arrays.asList(
                new Rol(null, "Administrador"),
                new Rol(null, "Gestor BD"),
                new Rol(null, "Usuario")
        );
        rolRepository.saveAll(roles);
    }
}