package org.batallanaval.backend.service.impl;

import org.batallanaval.backend.persistence.dao.GameDAO;
import org.batallanaval.backend.persistence.entity.Game;
import org.batallanaval.backend.service.interfaces.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameDAO gameDAO;

    @Override
    public List<Game> findAll() {
        return gameDAO.findAll();
    }

    @Override
    public Optional<Game> findById(UUID id) {
        return gameDAO.findById(id);
    }

    @Override
    public Game save(Game game) {
        return gameDAO.save(game);
    }

    @Override
    public void delete(UUID id) {
        gameDAO.deleteById(id);
    }
}
