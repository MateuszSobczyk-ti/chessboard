package com.sobczyk.chessboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer width;

    @Column(nullable = false)
    private Integer height;

//    @OneToMany(mappedBy = "game")
//    private List<Unit> units;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime start;

    private LocalDateTime stop;

    private boolean archived;
}
