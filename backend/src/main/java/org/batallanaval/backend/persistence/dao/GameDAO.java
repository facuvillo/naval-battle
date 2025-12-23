package org.batallanaval.backend.persistence.dao;

import org.batallanaval.backend.persistence.entity.Game;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameDAO {

    List<Game> findAll();
    Optional<Game> findById(UUID id);
    Game save(Game game);
    void deleteById(UUID id);
    // TODO Implementar algún método para buscar por jugador 1 o jugador 2
}
