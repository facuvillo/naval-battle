package org.batallanaval.backend.persistence.dao.impl;

import org.batallanaval.backend.persistence.dao.UserProfileDAO;
import org.batallanaval.backend.persistence.entity.UserProfile;
import org.batallanaval.backend.persistence.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserProfileDAOImpl implements UserProfileDAO {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public List<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    @Override
    public Optional<UserProfile> findById(UUID id) {
        return userProfileRepository.findById(id);
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public void deleteById(UUID id) {
        userProfileRepository.deleteById(id);
    }
}
