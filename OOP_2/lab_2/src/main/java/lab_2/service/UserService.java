package lab_2.service;

import lab_2.persistence.entity.User;
import lab_2.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class UserService {

    private UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() { return  userRepository.findAll(); }
}
