package org.batallanaval.backend.service.impl;

import org.batallanaval.backend.persistence.dao.impl.UserProfileDAOImpl;
import org.batallanaval.backend.persistence.entity.UserProfile;
import org.batallanaval.backend.service.interfaces.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileDAOImpl userProfileDAO;

    @Override
    public List<UserProfile> findAll() {
        return userProfileDAO.findAll();
    }

    @Override
    public Optional<UserProfile> findById(UUID id) {
        return userProfileDAO.findById(id);
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        return userProfileDAO.save(userProfile);
    }

    @Override
    public void delete(UUID id) {
        userProfileDAO.deleteById(id);
    }
}
