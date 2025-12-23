package org.batallanaval.backend.service.interfaces;

import org.batallanaval.backend.persistence.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(UUID id);
    User save(User user);
    void delete(UUID id);
}
