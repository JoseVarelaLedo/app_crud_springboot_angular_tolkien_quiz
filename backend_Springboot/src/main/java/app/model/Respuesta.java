package app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "respuesta")

public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "texto_respuesta", nullable = false, length = 300)
    private String textoRespuesta;

    @Column(name = "es_respuesta_correcta", nullable = false)
    private Boolean esRespuestaCorrecta;

    @ManyToOne
    @JoinColumn (name = "pregunta_id", referencedColumnName = "id")
    @JsonIgnore
    private Pregunta pregunta;
}
