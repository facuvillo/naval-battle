package org.batallanaval.backend.persistence.entity.gameLogic;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Board {

    private int width;
    private int height;
    private List<Ship> ships;
    private List<Coordinate> shots;
    private int lives;

    public Board () {
        this.width = 10;
        this.height = 10;
        this.ships = new ArrayList<>();
        this.shots = new ArrayList<>();
    }

    public void addShip(Ship ship) {
        if (!validateCondinate(ship.getPositions())){
            throw new IllegalArgumentException("Positions are inconsistent");
        }
        ships.add(ship);
        lives += ship.getType().getSize();
    }

    private boolean validateCondinate(List<Coordinate> coordinate) {
        for (Coordinate c : coordinate) {
            if (c.getX() < 0 || c.getX() > width || c.getY() < 0 || c.getY() > height) {
                return false;
            }
        }
        return true;
    }

}
