package org.batallanaval.backend.service.interfaces;

import org.batallanaval.backend.persistence.entity.UserProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileService {

    List<UserProfile> findAll();
    Optional<UserProfile> findById(UUID id);
    UserProfile save(UserProfile userProfile);
    void delete(UUID id);
}
