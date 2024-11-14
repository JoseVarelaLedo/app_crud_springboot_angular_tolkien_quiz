package app.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
