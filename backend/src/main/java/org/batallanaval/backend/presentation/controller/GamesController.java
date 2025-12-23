package org.batallanaval.backend.presentation.controller;

import org.batallanaval.backend.persistence.entity.gameLogic.GameLogic;
import org.batallanaval.backend.persistence.entity.gameLogic.Player;
import org.batallanaval.backend.service.impl.GameManager;
import org.batallanaval.backend.service.impl.MqttClientService;
import org.batallanaval.backend.util.dto.JoinGameData;
import org.batallanaval.backend.util.dto.JoinPlayerInfo;
import org.batallanaval.backend.util.dto.NewGameUserData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
public class GamesController {

    private final MqttClientService mqttClientService;
    private final GameManager gameManager;

    public GamesController(MqttClientService mqttClientService, GameManager gameManager) {
        this.mqttClientService = mqttClientService;
        this.gameManager = gameManager;
    }

    // TODO Cambiar GameManager por MqttClientService
    @GetMapping("/")
    public ResponseEntity<?> getGames() {
        List<JoinGameData> games = gameManager.getAllGames();
        return ResponseEntity.ok(games);
    }

    // TODO Hacer que pida un user
    @PostMapping("/newGame")
    public ResponseEntity<?> createNewGame(@RequestBody NewGameUserData newGameUserData) {
        Player player = new Player(UUID.fromString(newGameUserData.getPlayerUserId()), newGameUserData.getPlayerUsername());
        String gameTopicBase = mqttClientService.startGame(player);
        return ResponseEntity.ok(gameTopicBase);
    }

    @GetMapping("/status")
    public ResponseEntity<?> sendStatus(){
        return ResponseEntity.ok("Running");
    }

    @PostMapping("/joinGame")
    public ResponseEntity<?> joinPlayerToGame(@RequestBody JoinPlayerInfo  joinPlayerInfo) {
        String topicBase = mqttClientService.joinGame(joinPlayerInfo.getPlayerId(), joinPlayerInfo.getGameId());
        return ResponseEntity.ok(topicBase);
    }
}
