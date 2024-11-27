package app.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Clase DTO para simplificar la transmisión de datos relativos a la tabla de Usuario.
 * Como dicha tabla tiene dependencias con la tabla Fichas de Usuario, y Rol, al tener un DTO se envían datos simplificados
 * teniendo un DTO para ello, sin transmitir objetos de los tipos citados.  Las anotaciones son las propias de Lombok para ahorrar código,
 *  * sin explicitar constructores, y métodos accesores y modificadores standard.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nickname;
    private String contrasena;
    private Integer rolID;
    private String nombreRol;
    private Long fichaUsuarioId;
    private LocalDateTime fechaRegistro;
}