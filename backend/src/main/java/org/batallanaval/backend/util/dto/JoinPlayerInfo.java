package org.batallanaval.backend.util.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.batallanaval.backend.persistence.entity.gameLogic.Player;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinPlayerInfo {
    private String playerId;
    private String gameId;
}
