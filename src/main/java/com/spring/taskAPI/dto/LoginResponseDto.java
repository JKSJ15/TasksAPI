package com.spring.taskAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDto (@Schema(
		description = "JWT access token to be sent in the Authorization header",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20ifQ.signature"
		) String token){

}
