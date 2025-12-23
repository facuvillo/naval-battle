package org.batallanaval.backend.presentation.controller;

import org.batallanaval.backend.persistence.entity.gameLogic.Player;
import org.batallanaval.backend.service.impl.MqttClientService;
import org.batallanaval.backend.service.impl.UsersOnlineManager;
import org.batallanaval.backend.util.dto.JoinGameData;
import org.batallanaval.backend.util.dto.NewGameUserData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/players")
public class PlayerOnlineController {

    private final MqttClientService mqttClientService;
    private final UsersOnlineManager onlineManager;

    public PlayerOnlineController(MqttClientService mqttClientService, UsersOnlineManager onlineManager) {
        this.mqttClientService = mqttClientService;
        this.onlineManager = onlineManager;
    }

    @GetMapping("/allusers")
    public ResponseEntity<List<Player>> allPlayer() {
        //System.out.println(onlineManager.getAllPlayers());
        List<Player> players = onlineManager.getAllPlayers();
        return ResponseEntity.ok(players);
    }


    @PostMapping("/connect")
    public void connectPlayer(@RequestBody NewGameUserData player) {
        //System.out.println("CHECK 1");
        Player newPlayer = new Player(UUID.fromString(player.getPlayerUserId()), player.getPlayerUsername());
        onlineManager.addOnlinePlayer(newPlayer);
    }

    @PostMapping("/invite")
    public void invitePlayer(@RequestBody JoinGameData data) {

        UUID gameId = UUID.fromString(data.getGameId());
        String invitedPlayerId = data.getPlayerUserId();
        String inviterName = data.getPlayerUsername();

        mqttClientService.sendInvitation(invitedPlayerId, gameId, inviterName);
    }
}
