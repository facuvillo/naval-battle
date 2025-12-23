package org.batallanaval.backend.util.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewGameUserData {
    private String playerUserId;
    private String playerUsername;
}
