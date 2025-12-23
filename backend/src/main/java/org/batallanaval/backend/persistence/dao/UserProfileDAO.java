package org.batallanaval.backend.persistence.dao;

import org.batallanaval.backend.persistence.entity.UserProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileDAO {

    List<UserProfile> findAll();
    Optional<UserProfile> findById(UUID id);
    UserProfile save(UserProfile userProfile);
    void deleteById(UUID id);

}
