package org.batallanaval.backend.persistence.entity.gameLogic;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Coordinate {
    private int x;
    private int y;
}
