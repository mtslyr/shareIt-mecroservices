package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Repository
public interface UserStorage extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    void deleteById(Long userId);
}
