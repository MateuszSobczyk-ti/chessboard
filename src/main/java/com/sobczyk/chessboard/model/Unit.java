package com.sobczyk.chessboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Enumerated(EnumType.STRING)
    private Player player;

    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    @Column(name = "width_position", nullable = false)
    private Integer widthPosition;

    @Column(name = "height_position", nullable = false)
    private Integer heightPosition;

    private boolean destroyed;

    @OneToMany(mappedBy = "unit")
    private List<Command> commands;

    public enum Player {
        WHITE, BLACK
    }
}
