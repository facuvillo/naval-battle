package org.batallanaval.backend.service.interfaces;

import org.batallanaval.backend.persistence.entity.Game;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameService {
    List<Game> findAll();
    Optional<Game> findById(UUID id);
    Game save(Game game);
    void delete(UUID id);
}
