package app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para mapear la tabla de Fichas de Usuario en la Base de Datos.
 * Las anotaciones @Data, @Builder, @AllArgsConstructor y @NoArgsConstructor son
 * propias de Lombok, para ahorrar código,
 * y @Data, @Entity y @Table las propias de SpringBoot / Hibernate para mapeado de BD.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rol")
public class Rol {
    @Id                                                             // clave primaria autogenerada
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_rol", nullable = false, unique = true, length = 50)
    private String nombreRol;

}
