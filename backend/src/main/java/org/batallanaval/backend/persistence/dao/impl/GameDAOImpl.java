package org.batallanaval.backend.persistence.dao.impl;

import org.batallanaval.backend.persistence.dao.GameDAO;
import org.batallanaval.backend.persistence.entity.Game;
import org.batallanaval.backend.persistence.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GameDAOImpl implements GameDAO {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    @Override
    public Optional<Game> findById(UUID id) {
        return gameRepository.findById(id);
    }

    @Override
    public Game save(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public void deleteById(UUID id) {
        gameRepository.deleteById(id);
    }
}
