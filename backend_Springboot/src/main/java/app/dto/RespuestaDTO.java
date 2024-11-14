package app.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaDTO {
    private Long id;
    private String textoRespuesta;
    private boolean esRespuestaCorrecta;
    private Long preguntaId;

}
