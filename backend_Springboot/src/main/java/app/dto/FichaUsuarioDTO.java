package app.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase DTO para simplificar la transmisión de datos relativos a la tabla de Fichas de Usuario.
 * Como dicha tabla tiene dependencias con la tabla Usuario, al tener un DTO se envían datos simplificados
 * teniendo un DTO para ello, sin transmitir objetos de tipo Usuario. Las anotaciones son las propias de Lombok para ahorrar código,
 * sin explicitar constructores, y métodos accesores y modificadores standard.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FichaUsuarioDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String direccion;
    private String correoElectronico;
    private String nickname;
    private String password;
    private Long score;
    private LocalDate fechaNacimiento;
    private LocalDateTime fechaRegistro;

}
