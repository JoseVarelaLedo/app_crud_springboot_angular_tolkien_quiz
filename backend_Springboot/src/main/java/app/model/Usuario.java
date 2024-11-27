package app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase para mapear la tabla de Usuario en la Base de Datos.
 * Las anotaciones @Data, @Builder, @AllArgsConstructor y @NoArgsConstructor son
 * propias de Lombok, para ahorrar código,
 * y @Data, @Entity y @Table las propias de SpringBoot / Hibernate para mapeado de BD.
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "usuario")
public class Usuario {
    @Id                                                     // clave primaria autogenerada
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = false, length = 150)
    private String contrasena;

    @ManyToOne                                                      // relación muchos a uno, ya que un mismo rol puede ser asignado a muchos usuarios
    @JoinColumn(name = "rol_id", referencedColumnName = "id")
    @JsonIgnore
    private Rol rol;

    @OneToOne (cascade = CascadeType.ALL)                           // cada usuario se corresponde con una ficha de usuario
    @JoinColumn(name = "ficha_usuario", referencedColumnName = "id")
    @JsonIgnore
    private FichaUsuario fichaUsuario;

    @Column
    private LocalDateTime fechaRegistro;

    @PrePersist
    private void onCreate(){
        this.fechaRegistro = LocalDateTime.now();
    }
}
