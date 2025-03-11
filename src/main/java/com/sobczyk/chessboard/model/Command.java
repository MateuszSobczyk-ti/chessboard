package com.sobczyk.chessboard.model;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.dto.request.CoordinateDto;
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
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long gameId;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false, insertable = false, updatable = false)
    private Unit unit;

    @Enumerated(EnumType.STRING)
    private Unit.Player player;

    @Enumerated(EnumType.STRING)
    private ActionRequest.ActionType actionType;

    @Enumerated(EnumType.STRING)
    private CoordinateDto.Direction firstDirection;

    private Integer firstFields;

    @Enumerated(EnumType.STRING)
    private CoordinateDto.Direction secondDirection;

    private Integer secondFields;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime executionDate;
}
