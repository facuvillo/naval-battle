package org.batallanaval.backend.persistence.entity.gameLogic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Ship {
    private ShipType type;
    private boolean isSunk;
    private List<Coordinate> positions;
    private List<Coordinate> hits;

    public Ship(ShipType type, List<Coordinate> positions) {
        this.type = type;
        this.positions = positions;
        this.hits = new ArrayList<>();
        this.isSunk = false;
    }
}
