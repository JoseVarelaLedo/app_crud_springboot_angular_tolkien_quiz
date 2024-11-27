package app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Clase para mapear la tabla de Respuestas en la Base de Datos.
 * Las anotaciones @Data, @Builder, @AllArgsConstructor y @NoArgsConstructor son
 * propias de Lombok, para ahorrar código,
 * y @Data, @Entity y @Table las propias de SpringBoot / Hibernate para mapeado de BD.
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "respuesta")

public class Respuesta {
    @Id                                                       // clave primaria autogenerada
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "texto_respuesta", nullable = false, length = 300)
    private String textoRespuesta;

    @Column(name = "es_respuesta_correcta", nullable = false)
    private Boolean esRespuestaCorrecta;

    @ManyToOne                                                      // relación muchos a uno. Una Pregunta puede tener muchas respuestas
    @JoinColumn (name = "pregunta_id", referencedColumnName = "id")
    @JsonIgnore
    private Pregunta pregunta;
}
