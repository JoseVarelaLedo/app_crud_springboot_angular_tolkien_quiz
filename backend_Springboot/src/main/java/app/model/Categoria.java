package app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para mapear la tabla de Categorías en la Base de Datos.
 * Las anotaciones @Data, @Builder, @AllArgsConstructor y @NoArgsConstructor son
 * propias de Lombok, para ahorrar código,
 * y @Data, @Entity y @Table las propias de SpringBoot / Hibernate para mapeado de BD.
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name="categoria")

public class Categoria {

    @Id                                                     // clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // se autogenera la id de forma correlativa
    private Long id;

    @Column(name = "categoria_desc", nullable = false, length = 100)
    private String categoriaDesc;
}
