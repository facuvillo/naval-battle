package org.batallanaval.backend.persistence.entity.gameLogic;

import lombok.Data;
import org.batallanaval.backend.util.dto.WinnerData;

import java.util.*;

@Data
public class GameLogic {

    private UUID uuid;
    private List<Player> players;
    private int currentPlayerIndex;
    private Map<UUID, Board> boards;
    private WinnerData winnerData;
    private boolean finished;
    private int lives;

    public GameLogic(Player player) {
        this.uuid = UUID.randomUUID();
        this.players = new ArrayList<>();
        this.players.add(player);
        this.currentPlayerIndex = 0;
        this.boards = new HashMap<>();
        boards.put(player.getUuid(), player.getBoard());
        this.finished = false;
    }

    public boolean processShot(Coordinate coordinate) {
        boolean isHit = false;
        Board board;

        if (currentPlayerIndex == 0){
            board = this.players.getLast().getBoard();
        }else {
            board = this.players.getFirst().getBoard();
        }

        List<Ship> ships = board.getShips();
        if (!ships.isEmpty()) {
            for (Ship ship : ships) {
                List<Coordinate> positions = ship.getPositions();
                for (Coordinate position : positions) {
                    if (coordinate.equals(position)) {
                        ship.getHits().add(coordinate);
                        isHit = true;
                        break;
                    }
                }
                if (ship.getHits().size() >= ship.getPositions().size()) {
                    ship.setSunk(true);
                    break;
                }
            }
        }
        processGameOver(board);
        if (isFinished()){
            return true;
        }
        changeCurrentPlayerIndex();
        return isHit;
    }

    public void setShips(List<Ship> ships, String playerId) {
        Board board = null;
        for (Player player : players) {
            if (player.getUuid().toString().equals(playerId)) {
                board = player.getBoard();
            }
        }
        for (Ship ship : ships) {
            board.addShip(ship);
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void changeCurrentPlayerIndex() {
        if (currentPlayerIndex == 0) {
            currentPlayerIndex++;
        }else  {
            currentPlayerIndex = 0;
        }
    }

    public void addNewPlayer(Player player) {
        players.add(player);
        System.out.println("Jugador añadido");
    }

    private Player getOpponentPlayer() {
        if (currentPlayerIndex == 0) {
            return players.get(1);
        } else {
            return players.get(0);
        }
    }

    public void processGameOver(Board board) {
        int countHits = 0;
        List<Ship> ships = board.getShips();
        for (Ship ship : ships) {
            List<Coordinate> hits = ship.getHits();
            countHits += hits.size();
        }
        if (countHits == board.getLives()) {
            WinnerData winnerData = WinnerData.builder()
                    .command("FINISHED")
                    .winnerId(players.get(currentPlayerIndex).getUuid().toString())
                    .winnerName(players.get(currentPlayerIndex).getUsername())
                    .build();
            this.finished = true;
            this.winnerData = winnerData;
        }
    }



//    public boolean processGameOver() {
//
//        Player opponent = getOpponentPlayer();
//        if (opponent.getBoard().getShips().isEmpty()) {
//            System.out.println("Game Over");
//            this.finished = true;
//        }
//        return finished;
//    }
}
