package org.batallanaval.backend.service.impl;

import org.batallanaval.backend.persistence.entity.gameLogic.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsersOnlineManager {

    private final Map<UUID, Player> onlinePlayers = new ConcurrentHashMap<>();

    public void addOnlinePlayer(Player player) {
        onlinePlayers.put(player.getUuid(), player);
    }

    public void removeOnlinePlayer(UUID playerId) {
        onlinePlayers.remove(playerId);
    }

    public List<Player> getAllPlayers() {
        return new ArrayList<>(onlinePlayers.values());
    }

}
