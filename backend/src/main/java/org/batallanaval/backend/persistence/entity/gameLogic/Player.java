package org.batallanaval.backend.persistence.entity.gameLogic;

import lombok.Data;

import java.util.UUID;

@Data
public class Player {

    private UUID uuid;
    private String username;
    private Board board;

    public Player(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
        this.board = new Board();
    }

}
