package com.tennis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PlayerRequestDto(
        @NotBlank(message = "Name cannot be empty")
        @Size(min = 2, max = 30, message = "Name must be 2 - 30 characters long")
        @Pattern(regexp = "^[а-яА-яa-zA-Z'\\-\\s]+$", message = "Name must contain only letters, \" ' \", \" - \" and space")
        String name) {
}
