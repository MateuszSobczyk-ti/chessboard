package com.sobczyk.chessboard.mapper;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.model.Command;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommandMapper {
    CommandMapper INSTANCE = Mappers.getMapper(CommandMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(source = "firstCoordinateDto.direction", target = "firstDirection")
    @Mapping(source = "firstCoordinateDto.fields", target = "firstFields")
    @Mapping(source = "secondCoordinateDto.direction", target = "secondDirection")
    @Mapping(source = "secondCoordinateDto.fields", target = "secondFields")
    @Mapping(target = "executionDate", ignore = true)
    Command toCommand(ActionRequest request);
}
