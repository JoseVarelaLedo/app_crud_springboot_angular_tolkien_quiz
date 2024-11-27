package app.data;

import app.dto.FichaUsuarioDTO;
import app.model.FichaUsuario;
import app.repository.FichaUsuarioRepository;
import app.service.FichaUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.ApplicationRunner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Esta clase inicializa la tabla de fichas de usuario con los datos necesarios;
 * se inicia en cuarto, y último, lugar dentro de las clases inicializadoras ya que hemos
 * anotado con @Order(4), ya que necesitamos que previamente se haya iniciado y llenado
 * la tabla que almacena los roles.  Se anota con @Order para que el framework Spring la trate de acuerdo a su naturaleza.
 */
@Component
@Order(4)
@DependsOn("initializerRol")
public class DataInitializerFichasUsuario {

    @Autowired
    private FichaUsuarioRepository fichaUsuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FichaUsuarioService fichaUsuarioService;

    @Bean
    public ApplicationRunner initializerFichasUsuario(FichaUsuarioRepository fichaUsuarioRepository) {
        return args -> {
            if (fichaUsuarioRepository.count() == 0) {
                insertarUsuarios(this.fichaUsuarioRepository);
            }
        };
    }

    public void insertarUsuarios(FichaUsuarioRepository fichaUsuarioRepository) {
        List<FichaUsuarioDTO> usuariosDTO = Arrays.asList(
                new FichaUsuarioDTO(null, "Jose", "Ledo", "654376543", "Casa de Jose, 1", "jose@ledo.com", "joseLedo", passwordEncoder.encode("password"), 0L, LocalDate.parse("1978-06-08"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Carlos", "Sánchez", "123123123", "Calle Esperanza 456, Villa Real", "carlos.sanchez@example.com", "carlosito", passwordEncoder.encode("seguro123"), 0L, LocalDate.parse("1985-03-22"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Laura", "Fernández", "987321654", "Camino Real 89, Pueblo Nuevo", "laura.fernandez@example.com", "laurita99", passwordEncoder.encode("pass1234"), 0L, LocalDate.parse("1992-11-30"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Luis", "Martínez", "123987654", "Calle Principal 101, Ciudad Antigua", "luis.martinez@example.com", "luismart", passwordEncoder.encode("claveSegura123"), 0L, LocalDate.parse("1989-01-17"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Ana", "López", "456789123", "Avenida del Sol 202, Barrio Central", "ana.lopez@example.com", "anita", passwordEncoder.encode("miClaveSecreta"), 0L, LocalDate.parse("1993-09-25"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Jorge", "Ruiz", "987456321", "Calle Luna 303, Ciudad Vieja", "jorge.ruiz@example.com", "jorgeruiz", passwordEncoder.encode("contrasenaTop"), 0L, LocalDate.parse("1986-08-02"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Elena", "Pérez", "321654987", "Boulevard de los Sueños 404, Colonia Nueva", "elena.perez@example.com", "elenaLove", passwordEncoder.encode("superClave"), 0L, LocalDate.parse("1991-02-28"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Pablo", "Ramírez", "654123987", "Camino de la Montaña 505, Pueblo Verde", "pablo.ramirez@example.com", "pablito", passwordEncoder.encode("contrasena123"), 0L, LocalDate.parse("1988-05-05"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Natalia", "Torres", "789654123", "Calle de la Paz 606, Villa Hermosa", "natalia.torres@example.com", "natyTorres", passwordEncoder.encode("miClavePersonal"), 0L, LocalDate.parse("1994-12-12"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Andrés", "Díaz", "456123789", "Paseo de los Olivos 707, Barrio del Lago", "andres.diaz@example.com", "andresD", passwordEncoder.encode("claveAndres"), 0L, LocalDate.parse("1982-07-18"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Rosa", "Morales", "741852963", "Calle de los Cerezos 808, Ciudad Real", "rosa.morales@example.com", "rosita123", passwordEncoder.encode("rosaClave"), 0L, LocalDate.parse("1980-04-09"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Roberto", "Jiménez", "963258741", "Avenida del Bosque 909, Pueblo Azul", "roberto.jimenez@example.com", "robertJ", passwordEncoder.encode("claveR1"), 0L, LocalDate.parse("1993-11-11"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Isabel", "Ortiz", "159357852", "Calle Jardines 1010, Ciudad Norte", "isabel.ortiz@example.com", "isabelO", passwordEncoder.encode("contrasenaIO"), 0L, LocalDate.parse("1985-06-06"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Francisco", "Castro", "258369147", "Camino de las Flores 1111, Barrio Unido", "francisco.castro@example.com", "franCastro", passwordEncoder.encode("passwordCastro"), 0L, LocalDate.parse("1979-10-01"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Sara", "Ríos", "753951852", "Boulevard de la Luna 1212, Ciudad Joven", "sara.rios@example.com", "saraR", passwordEncoder.encode("claveRios"), 0L, LocalDate.parse("1992-03-15"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Gabriel", "Gómez", "654987321", "Avenida las Palmas 1313, Colonia Sur", "gabriel.gomez@example.com", "gabo123", passwordEncoder.encode("passGomez"), 0L, LocalDate.parse("1991-08-20"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Lorena", "Vargas", "123654789", "Calle Diamante 1414, Ciudad Nueva", "lorena.vargas@example.com", "lorenita", passwordEncoder.encode("passVargas"), 0L, LocalDate.parse("1990-05-14"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Santiago", "Mendoza", "321789456", "Paseo de la Victoria 1515, Pueblo Unido", "santiago.mendoza@example.com", "santiM", passwordEncoder.encode("claveMendoza"), 0L, LocalDate.parse("1986-01-30"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Juliana", "Fuentes", "852963147", "Calle de los Árboles 1616, Barrio Antiguo", "juliana.fuentes@example.com", "juliF", passwordEncoder.encode("claveFuentes"), 0L, LocalDate.parse("1984-12-20"), LocalDateTime.now()),
                new FichaUsuarioDTO(null, "Alberto", "Guerrero", "951753258", "Avenida del Parque 1717, Ciudad Soleada", "alberto.guerrero@example.com", "betoG", passwordEncoder.encode("claveGuerrero"), 0L, LocalDate.parse("1987-09-19"), LocalDateTime.now())
        );

        // Convertir cada FichaUsuarioDTO a FichaUsuario antes de guardar
        List<FichaUsuario> usuarios = usuariosDTO.stream()
                .map(this::convertirDTOAUsuario)
                .collect(Collectors.toList());

        usuarios.forEach(this.fichaUsuarioService::sincronizarCreacionEntidadesPublic);

        fichaUsuarioRepository.saveAll(usuarios);
    }

    // conversión DTO a usuarios

    private FichaUsuario convertirDTOAUsuario(FichaUsuarioDTO dto) {
        FichaUsuario usuario = new FichaUsuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellidos(dto.getApellidos());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        usuario.setNickname(dto.getNickname());
        usuario.setPassword(dto.getPassword());
        usuario.setScore(dto.getScore());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setFechaRegistro(dto.getFechaRegistro());
        return usuario;
    }
}