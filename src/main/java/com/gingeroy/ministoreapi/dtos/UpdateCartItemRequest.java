package com.gingeroy.ministoreapi.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1)
    @Max(value = 100)
    private Integer quantity;
}
