package com.spring.taskAPI.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @NotBlank(message = "title of task cannot be empty")
    private String title;

    @Column(name = "description")
    @NotBlank(message = "description cannot be empty")
    private String description;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "status cannot be null")
    private Status status;

    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "priority cannot be null")
    private Priority priority;

    @Column(name = "comp_date")
    private LocalDate dateConcl;

    @Column(name = "deadline")
    @NotNull(message = "deadline cannot be null")
    private LocalDate deadline;

    @Column(name = "date_creation")
    @NotNull(message = "createdAt cannot be null")
    private LocalDate createdAt;

	public Task() {}

	private Task(Builder builder) {
		this.id = builder.id;
		this.title = builder.title;
		this.description = builder.description;
		this.status = builder.status;
		this.priority = builder.priority;
		this.dateConcl = builder.dateConcl;
		this.deadline = builder.deadline;
		this.createdAt = builder.createdAt;
	}

	
	public LocalDate getDateConcl() {
		return dateConcl;
	}
	public void setDateConcl(LocalDate dateConcl) {
		this.dateConcl = dateConcl;
	}
	public LocalDate getDeadline() {
		return deadline;
	}
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	public LocalDate getCreatedAt() {
		return createdAt;
	}
	public Long getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Priority getPriority() {
		return priority;
	}
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	public static Builder builder() {
		return new Builder();
	}
	public static final class Builder {
		private Long id;
		private String title;
		private String description;
		private Status status;
		private Priority priority;
		private LocalDate dateConcl;
		private LocalDate deadline;
		private LocalDate createdAt;

		private Builder() {
		}

		public Builder withId(Long id) {
			this.id = id;
			return this;
		}

		public Builder withTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder withStatus(Status status) {
			this.status = status;
			return this;
		}

		public Builder withPriority(Priority priority) {
			this.priority = priority;
			return this;
		}

		public Builder withDateConcl(LocalDate dateConcl) {
			this.dateConcl = dateConcl;
			return this;
		}

		public Builder withDeadline(LocalDate deadline) {
			this.deadline = deadline;
			return this;
		}

		public Builder withCreatedAt(LocalDate createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public Task build() {
			return new Task(this);
		}
	}

	

}
