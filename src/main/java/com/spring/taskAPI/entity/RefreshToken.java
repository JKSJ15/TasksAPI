package com.spring.taskAPI.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, unique = true, length = 500)
	private String refreshToken;

	@JoinColumn(name = "user_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Column(nullable = false)
	private Instant expiresAt;

	private RefreshToken(Builder builder) {
		this.id = builder.id;
		this.refreshToken = builder.refreshToken;
		this.user = builder.user;
		this.expiresAt = builder.expiresAt;
	}

	public RefreshToken() {
	}

	public boolean isExpired(Instant instant) {
		return Instant.now().isAfter(instant);
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public User getUser() {
		return user;
	}

	public Instant getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Instant expiresAt) {
		this.expiresAt = expiresAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private long id;
		private String refreshToken;
		private User user;
		private Instant expiresAt;

		private Builder() {
		}

		public Builder withId(long id) {
			this.id = id;
			return this;
		}

		public Builder withRefreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
			return this;
		}

		public Builder withUser(User user) {
			this.user = user;
			return this;
		}

		public Builder withExpiresAt(Instant expiresAt) {
			this.expiresAt = expiresAt;
			return this;
		}

		public RefreshToken build() {
			return new RefreshToken(this);
		}
	}

}
