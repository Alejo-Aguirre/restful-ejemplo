package co.edu.uniquindio.ingesis.repository;

import co.edu.uniquindio.ingesis.model.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository {
    private List<User> users = new ArrayList<>();
    private Integer nextId = 1;

    public User save(User user) {
        user.setId(nextId++);
        users.add(user);
        return user;
    }

    public Optional<User> findById(Integer id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public List<User> findAll() {
        return users;
    }

    public void deleteById(Integer id) {
        users.removeIf(u -> u.getId().equals(id));
    }

    public Optional<User> findByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }
}
