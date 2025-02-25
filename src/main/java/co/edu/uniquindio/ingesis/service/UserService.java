package co.edu.uniquindio.ingesis.service;

import co.edu.uniquindio.ingesis.dto.UserRegistrationRequest;
import co.edu.uniquindio.ingesis.model.User;
import co.edu.uniquindio.ingesis.repository.UserRepository;
import co.edu.uniquindio.ingesis.security.JWTUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public User registerUser(UserRegistrationRequest request) {
        if (request.email() == null || request.clave() == null || request.nombreUsuario() == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        Optional<User> existingUser = userRepository.findByEmail(request.email());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setClave(hashPassword(request.clave())); // Hashear la contraseña
        user.setUsuario(request.nombreUsuario());
        user.setRol(request.rol()); // Asignar el rol proporcionado (o USER por defecto)

        return userRepository.save(user);
    }

    public List<User> getAllUsers(int page, int limit, String email) {
        List<User> users = userRepository.findAll();

        if (email != null && !email.isEmpty()) {
            users = users.stream().filter(u -> u.getEmail().equals(email)).toList();
        }

        int fromIndex = (page - 1) * limit;
        int toIndex = Math.min(fromIndex + limit, users.size());

        return users.subList(fromIndex, toIndex);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Integer id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return null;
        }

        User updatedUser = existingUser.get();
        updatedUser.setUsuario(user.getUsuario());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setClave(hashPassword(user.getClave())); // Hashear la nueva contraseña
        updatedUser.setRol(user.getRol());

        return updatedUser;
    }

    public User partialUpdateUser(Integer id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return null;
        }

        User updatedUser = existingUser.get();
        if (user.getUsuario() != null) updatedUser.setUsuario(user.getUsuario());
        if (user.getEmail() != null) updatedUser.setEmail(user.getEmail());
        if (user.getClave() != null) updatedUser.setClave(hashPassword(user.getClave())); // Hashear la nueva contraseña
        if (user.getRol() != null) updatedUser.setRol(user.getRol());

        return updatedUser;
    }

    public boolean deleteUser(Integer id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return false;
        }

        userRepository.deleteById(id);
        return true;
    }

    public String login(String email, String clave) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && BCrypt.checkpw(clave, user.get().getClave())) {
            return JWTUtil.generateToken(user.get().getEmail(), user.get().getRol().name()); // Generar token JWT con rol
        }
        return null;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt()); // Hashear la contraseña
    }
}