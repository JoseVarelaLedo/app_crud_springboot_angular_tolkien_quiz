package app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Clase para mapear la tabla de Preguntas en la Base de Datos.
 * Las anotaciones @Data, @Builder, @AllArgsConstructor y @NoArgsConstructor son
 * propias de Lombok, para ahorrar código,
 * y @Data, @Entity y @Table las propias de SpringBoot / Hibernate para mapeado de BD.
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name="pregunta")

public class Pregunta {
    @Id                                                     // clave primaria autogenerada
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "texto_pregunta", nullable = false, length = 300)
    private String textoPregunta;

    @Column(name = "es_pregunta_quesito", nullable = false)
    private Boolean esPreguntaQuesito;

    @Column(name = "es_pregunta_ronda_final", nullable = false)
    private Boolean esPreguntaRondaFinal;

    @ManyToOne                                                          // relación muchos a uno, una categoría puede tener muchas preguntas asociadas
    @JoinColumn (name = "categoria_id", referencedColumnName = "id")

    private Categoria categoria;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL)            // relación uno a muchos, una pregunta puede tener muchas respuestas
    private List<Respuesta> respuestas;  // agregamos la lista de respuestas

}
