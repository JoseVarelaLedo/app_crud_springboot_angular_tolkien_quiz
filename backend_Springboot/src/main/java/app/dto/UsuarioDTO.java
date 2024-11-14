package app.dto;

import lombok.*;

import java.time.LocalDateTime;

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