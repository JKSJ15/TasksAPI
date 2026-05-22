package com.spring.taskAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenRefreshRequestDto(
		@Schema(description = "JWT refresh token to be sent in the endpoint /refresh in the Authorization header", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20ifQ.signature")
		String refreshToken) {

}
