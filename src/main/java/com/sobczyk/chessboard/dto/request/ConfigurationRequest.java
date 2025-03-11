package com.sobczyk.chessboard.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ConfigurationRequest(@NotNull(message = "Width dimension cannot be empty")
                                   @Min(message = "dimension value must be greater than 1", value = 1)
                                   Integer width,
                                   @NotNull(message = "Height dimension cannot be empty")
                                   @Min(message = "dimension value must be greater than 1", value = 1)
                                   Integer height,
                                   UnitsConfigurationDto whiteUnits,
                                   UnitsConfigurationDto blackUnits) {
}
