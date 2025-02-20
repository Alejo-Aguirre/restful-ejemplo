package co.edu.uniquindio.ingesis.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class User {
        private Integer id;
        private String usuario;
        private String email;
        private String clave;
}
