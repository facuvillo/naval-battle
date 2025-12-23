package org.batallanaval.backend.service.impl;

import org.batallanaval.backend.persistence.entity.User;
import org.batallanaval.backend.persistence.entity.gameLogic.Coordinate;
import org.batallanaval.backend.persistence.entity.gameLogic.GameLogic;
import org.batallanaval.backend.persistence.entity.gameLogic.Player;
import org.batallanaval.backend.persistence.entity.gameLogic.Ship;
import org.batallanaval.backend.persistence.repository.UserRepository;
import org.batallanaval.backend.util.dto.JoinGameData;
import org.batallanaval.backend.util.dto.WinnerData;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameManager {

    private final Map<UUID, GameLogic> activeGames = new ConcurrentHashMap<>();
    private final UserRepository userRepository;

    public GameManager(UserProfileServiceImpl userProfileServiceImpl, UserServiceImpl userServiceImpl, UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public GameLogic createGamelogic(Player player) {
        GameLogic gameLogic = new GameLogic(player);
        activeGames.put(gameLogic.getUuid(), gameLogic);
        return gameLogic;
    }

    public GameLogic getGameLogic(UUID uuid) {
        return activeGames.get(uuid);
    }

    public List<JoinGameData> getAllGames(){
        List<GameLogic> games = new ArrayList<>(activeGames.values());
        List<JoinGameData> gamesData = new ArrayList<>();
        for(GameLogic game : games){
            if (game.getPlayers().size() == 1 ){
                System.out.println();
                JoinGameData joinGameData = JoinGameData.builder()
                        .gameId(game.getUuid().toString())
                        .playerUsername(game.getPlayers().getFirst().getUsername())
                        .playerUserId(game.getPlayers().getFirst().getUuid().toString())
                        .baseTopic("battleship/games/" + game.getUuid().toString())
                        .build();
                gamesData.add(joinGameData);
            }
        }
        return gamesData;
    }

    public String joinGame(String playerId, String gameId) {
        UUID playerUuid = UUID.fromString(playerId);
        UUID gameUuid = UUID.fromString(gameId);

        Optional<User> user = userRepository.findById(playerUuid);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        GameLogic gameLogic = activeGames.get(gameUuid);
        if (gameLogic == null) {
            throw new RuntimeException("Game not found");
        }

        Player player = new Player(playerUuid, user.get().getUsername());
        gameLogic.getPlayers().add(player);

        return "battleship/games/" + gameId;
    }

    public boolean processShot(String gameId, Coordinate coordinate) {
        UUID gameUuid = UUID.fromString(gameId);
        GameLogic game = activeGames.get(gameUuid);

        if (game == null) {
            throw new RuntimeException("Game not found");
        }

        return game.processShot(coordinate);
    }

    public WinnerData processGameOver(String gameId){
        GameLogic game = activeGames.get(UUID.fromString(gameId));
        if (game == null) {
            throw new RuntimeException("Game not found");
        }
        if (game.isFinished()){
            return game.getWinnerData();
        }
        return null;
    }


    public void setShips (String gameId, List<Ship> ships, String playerId) {
        UUID gameUuid = UUID.fromString(gameId);
        GameLogic game = activeGames.get(gameUuid);

        if (game == null) {
            throw new RuntimeException("Game not found");
        }

        game.setShips(ships, playerId);
    }

    public void removeGame(String gameId) {
        UUID gameUuid = UUID.fromString(gameId);
        activeGames.remove(gameUuid);
    }
}
