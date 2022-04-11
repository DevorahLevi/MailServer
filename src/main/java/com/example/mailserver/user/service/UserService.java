package com.example.mailserver.user.service;

import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User findById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void updatePassword(String userId, String password) {
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(EntityNotFoundException::new);

        user.setPassword(password);
        userRepository.save(user);
    }
}
