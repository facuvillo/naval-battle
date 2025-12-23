package org.batallanaval.backend.util.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinnerData {
    private String command;
    private String winnerId;
    private String winnerName;
}
