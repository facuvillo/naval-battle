package org.batallanaval.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "userprofiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int ranking;

    @ManyToMany(mappedBy = "players", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Game> games = new ArrayList<>();
}
