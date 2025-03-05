package co.edu.uniquindio.ingesis.service;

import co.edu.uniquindio.ingesis.domain.Rol;
import co.edu.uniquindio.ingesis.dto.UserRegistrationRequest;
import co.edu.uniquindio.ingesis.model.User;
import co.edu.uniquindio.ingesis.repository.UserRepository;
import co.edu.uniquindio.ingesis.security.JWTUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Transactional
    public User registerUser(UserRegistrationRequest request) {
        if (request.email() == null || request.clave() == null || request.usuario() == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setClave(hashPassword(request.clave()));
        user.setUsuario(request.usuario());
        user.setRol(request.rol());

        userRepository.persist(user);
        return user;
    }

    public List<User> getAllUsers(int page, int limit, String email) {
        if (email != null && !email.isEmpty()) {
            return userRepository.find("email", email).list();
        }
        return userRepository.findAll().page(page - 1, limit).list();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            return null;
        }

        existingUser.setUsuario(user.getUsuario());
        existingUser.setEmail(user.getEmail());
        existingUser.setClave(hashPassword(user.getClave()));
        existingUser.setRol(user.getRol());

        return existingUser;
    }

    public User partialUpdateUser(Long id, User user) {
        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            return null;
        }

        if (user.getUsuario() != null) existingUser.setUsuario(user.getUsuario());
        if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
        if (user.getClave() != null) existingUser.setClave(hashPassword(user.getClave()));
        if (user.getRol() != null) existingUser.setRol(user.getRol());

        return existingUser;
    }

    public boolean deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    public String login(String email, String clave) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && BCrypt.checkpw(clave, user.get().getClave())) {
            return JWTUtil.generateToken(user.get().getEmail(), user.get().getRol().name());
        }
        return null;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
