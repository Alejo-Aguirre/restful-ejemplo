package co.edu.uniquindio.ingesis.service;

import co.edu.uniquindio.ingesis.model.User;
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
            return userRepository.save(user);
        }

        public List<User> getAllUsers() {
            return userRepository.findAll();
        }

        public Optional<User> getUserById(Integer id) {
            return userRepository.findById(id);
        }

        public void updateUser(Integer id, User user) {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User updatedUser = existingUser.get();
                updatedUser.setUsuario(user.getUsuario());
                updatedUser.setEmail(user.getEmail());
                updatedUser.setClave(user.getClave());
            }
        }

        public void deleteUser(Integer id) {
            userRepository.deleteById(id);
        }

        public Optional<User> login(String email, String clave) {
            return userRepository.findByEmail(email)
                    .filter(user -> user.getClave().equals(clave));
        }
    }

