package app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase para mapear la tabla de Fichas de Usuario en la Base de Datos.
 * Las anotaciones @Data, @Builder, @AllArgsConstructor y @NoArgsConstructor son
 * propias de Lombok, para ahorrar código,
 * y @Data, @Entity y @Table las propias de SpringBoot / Hibernate para mapeado de BD.
 * @Table define asimismo en sus atributos las restricciones de unicidad, en este caso,
 * aunque se podrían definir otras.
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name="fichaUsuario",uniqueConstraints = {
        @UniqueConstraint(columnNames = "nickname"),
        @UniqueConstraint(columnNames = "correo_electronico")
})
public class FichaUsuario {
    @Id                                                     // clave primaria autogenerada
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "telefono", length = 15, nullable = false)
    private String telefono;

    @Column(name = "direccion", length = 255, nullable = false)
    private String direccion;

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 100)
    private String correoElectronico;

    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(name = "password_usuario", nullable = false, length = 150)
    private String password;

    @Column(name = "score")
    private Long score;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    @OneToOne(mappedBy = "fichaUsuario", cascade = CascadeType.ALL)     // relación con tabla usuarios
    @JoinColumn (name = "usuario", referencedColumnName = "id")         // indicando la propiedad en la que se actúa como clave foránea
    @JsonIgnore                                                        // para que no se envíe el objeto de entidad Usuario en el Json, o se complejiza haciéndolo inmanejable
    private Usuario usuario;

    private LocalDateTime fechaRegistro;

    /**
     * Método de callback ejecutado antes de que una entidad sea persistida en la base de datos.
     *
     * <p>Este método inicializa el campo {@code fechaRegistro} con la fecha y hora actuales
     * utilizando {@link LocalDateTime#now()}. Es útil para establecer automáticamente
     * la marca de tiempo de creación de una entidad al momento de ser guardada por primera vez.</p>
     *
     * <p>La anotación {@code @PrePersist} indica que este método será invocado automáticamente
     * por el ciclo de vida de JPA antes de realizar la operación de persistencia.</p>
     */
    @PrePersist
    private void onCreate(){
        this.fechaRegistro = LocalDateTime.now();
    }
}
