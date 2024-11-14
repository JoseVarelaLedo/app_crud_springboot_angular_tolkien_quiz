package app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name="pregunta")

public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "texto_pregunta", nullable = false, length = 300)
    private String textoPregunta;

    @Column(name = "es_pregunta_quesito", nullable = false)
    private Boolean esPreguntaQuesito;

    @Column(name = "es_pregunta_ronda_final", nullable = false)
    private Boolean esPreguntaRondaFinal;

    @ManyToOne
    @JoinColumn (name = "categoria_id", referencedColumnName = "id")

    private Categoria categoria;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL)
    private List<Respuesta> respuestas;  // Agregando la lista de respuestas

}
