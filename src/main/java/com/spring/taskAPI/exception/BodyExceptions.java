package com.spring.taskAPI.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class BodyExceptions {
	private String message;
	private LocalDateTime timestamp;
	private HttpStatus erro;
	private int status;

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
