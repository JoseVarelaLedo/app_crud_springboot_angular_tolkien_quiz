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


@Component
@Order(3)
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