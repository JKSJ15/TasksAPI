package com.spring.taskAPI.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Body response error")
public class BodyExceptions {
	@Schema(description = "Error Message", example = "Task not found")
	private String message;
	@Schema(description = "Date and time of error", example = "2026-04-23T10:15:30")
	private LocalDateTime timestamp;
	@Schema(description = "HTTP error name", example = "NOT_FOUND")
	private HttpStatus erro;
	@Schema(description = "HTTP status code", example = "404")
	private int status;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public HttpStatus getErro() {
		return erro;
	}

	public void setErro(HttpStatus erro) {
		this.erro = erro;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	private BodyExceptions(Builder builder) {
		this.message = builder.message;
		this.timestamp = builder.timestamp;
		this.erro = builder.erro;
		this.status = builder.status;
	}
	
	public BodyExceptions(String message, LocalDateTime timestamp, HttpStatus erro, int status) {
		super();
		this.message = message;
		this.timestamp = timestamp;
		this.erro = erro;
		this.status = status;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String message;
		private LocalDateTime timestamp;
		private HttpStatus erro;
		private int status;

		private Builder() {
		}

		public Builder withMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder withTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder withErro(HttpStatus erro) {
			this.erro = erro;
			return this;
		}

		public Builder withStatus(int status) {
			this.status = status;
			return this;
		}

		public BodyExceptions build() {
			return new BodyExceptions(this);
		}
	}
	
	
	
}
