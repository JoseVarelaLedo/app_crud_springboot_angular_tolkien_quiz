package app.dto;

import lombok.*;

/**
 * Clase DTO para simplificar la transmisión de datos relativos a la tabla de respuestas.
 * Como dicha tabla tiene dependencias con las tablas de categorías y respuestas, por lo que, al tener un DTO, se envían datos simplificados
 * sin transmitir objetos de tipo Categoría y Respuesta.  Las anotaciones son las propias de Lombok para ahorrar código,
 *  * sin explicitar constructores, y métodos accesores y modificadores standard.
 */
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
