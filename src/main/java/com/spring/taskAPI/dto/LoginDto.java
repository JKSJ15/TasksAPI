package com.spring.taskAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginDto(
		@NotBlank(message = "login cannot be empty") @Schema(description = "User login", example = "example@email.com") String login,

		@NotBlank(message = "password cannot be empty") @Schema(description = "User password", example = "example123") String password) {

}
