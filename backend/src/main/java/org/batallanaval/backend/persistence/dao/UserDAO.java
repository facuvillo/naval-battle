package org.batallanaval.backend.persistence.dao;

import org.batallanaval.backend.persistence.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDAO {

    List<User> findAll();

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    User  save(User user);

    void delete(UUID id);
}
