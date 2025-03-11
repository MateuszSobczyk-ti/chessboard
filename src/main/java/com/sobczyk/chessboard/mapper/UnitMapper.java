package com.sobczyk.chessboard.mapper;

import com.sobczyk.chessboard.dto.response.UnitsResponse;
import com.sobczyk.chessboard.model.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UnitMapper {

    UnitMapper INSTANCE = Mappers.getMapper(UnitMapper.class);

    @Mapping(expression = "java(unit.getCommands() != null ? unit.getCommands().size() : 0)", target = "commandCounter")
    UnitsResponse toUnitResponse(Unit unit);

    List<UnitsResponse> toResponseList(List<Unit> units);
}
