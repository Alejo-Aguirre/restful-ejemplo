package co.edu.uniquindio.ingesis.model;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import co.edu.uniquindio.ingesis.domain.Rol;
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends PanacheEntityBase {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        private String usuario;
        private String email;
        private String clave;
        @Enumerated(EnumType.STRING)
        private Rol rol;
}