package co.edu.uniquindio.ingesis.service;

import co.edu.uniquindio.ingesis.model.User;
import co.edu.uniquindio.ingesis.model.TokenResponse;
import co.edu.uniquindio.ingesis.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public User registerUser(User user) {
        if (user.getEmail() == null || user.getClave() == null || user.getUsuario() == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

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
        updatedUser.setClave(user.getClave());

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
        if (user.getClave() != null) updatedUser.setClave(user.getClave());

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
        if (user.isPresent() && user.get().getClave().equals(clave)) {
            return generateToken(user.get()); // Simulación de un token
        }
        return null;
    }

    private String generateToken(User user) {
        return "TOKEN_" + user.getId(); // Simulación de un token JWT
    }
}
