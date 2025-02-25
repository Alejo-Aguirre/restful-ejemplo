package co.edu.uniquindio.ingesis.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import co.edu.uniquindio.ingesis.domain.Rol;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
        private Integer id;
        private String usuario;
        private String email;
        private String clave;
        private Rol rol; // Campo para el rol del usuario
}