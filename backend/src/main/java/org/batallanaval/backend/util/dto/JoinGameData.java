package org.batallanaval.backend.util.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinGameData {

    private String gameId;
    private String playerUsername;
    private String playerUserId;
    private String baseTopic;

}
