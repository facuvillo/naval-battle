package org.batallanaval.backend.service.impl;

import org.batallanaval.backend.persistence.dao.UserDAO;
import org.batallanaval.backend.persistence.dao.impl.UserDAOImpl;
import org.batallanaval.backend.persistence.entity.User;
import org.batallanaval.backend.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAOImpl userDAO;

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userDAO.findById(id);
    }

    @Override
    public User save(User user) {
        return userDAO.save(user);
    }

    @Override
    public void delete(UUID id) {
        userDAO.delete(id);
    }
}
